import 'dart:html';
import 'dart:math';
import 'dart:ui';
import 'dart:ui' as ui;

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_ui/msg.dart';
import 'package:flutter_ui/msg.dart' as msg;
import 'package:flutter_riverpod/flutter_riverpod.dart';

void main() {
  runApp(MaterialApp(
      title: 'Zauberer',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: TrickView()
  )
  );
  // runApp(WizardApp());
}

/// Observable list of open games displayed in the [GameListWidget].
class GameList extends ChangeNotifier {

  Map<String, ObservableGame> openGames = {};

  void addGame(Game newGame) {
    openGames[newGame.gameId] = ObservableGame(newGame);
    notifyListeners();
  }

  void removeGame(String gameId) {
    openGames.remove(gameId);
    notifyListeners();
  }

  void setGames(List<Game> games) {
    openGames.clear();
    for (var g in games) {
      addGame(g);
    }
    notifyListeners();
  }

  ObservableGame? join(String gameId, Player player) {
    var game = openGames[gameId];
    game?.addPlayer(player);
    return game;
  }

  void leave(String gameId, String playerId) {
    openGames[gameId]?.removePlayer(playerId);
  }

}

/// Wrapper around a [Game] that notifies its listeners, whenever the players of the game change.
/// [GameList] holds all open [ObservableGame]s being displayed in the [GameListWidget].
class ObservableGame extends ChangeNotifier {
  Game game;
  
  ObservableGame(this.game);

  List<Player> get players => game.players;

  void addPlayer(Player player) {
    game.players.add(player);
    notifyListeners();
  }

  void removePlayer(String playerId) {
    game.players.removeWhere((p) => p.id == playerId);
    notifyListeners();
  }
}

/// State of a [ConnectionHandler].
enum ConnectionState {
  /// The player has not yet started logging in.
  idle,

  /// The [WebSocket] connection has been created but was not yet confirmed by
  /// the server.
  connecting,

  /// The [Welcome] message has been receive.
  connected,

  /// The player has not yet joined a game and is listening for other players
  /// opening games.
  searchingGame,

  /// The player has joined a game. The game has not been started yet, because
  /// the players are waiting for more members to join the game.
  waitingForStart,

  /// The game has started.
  playing,

  /// The player lost the server connection but is able to [Reconnect] using
  /// its player ID
  disconnected
}

final connection = ConnectionHandler();

/// Handler of the server connection.
/// Allows to send [Cmd]s through [sendCommand] and dispatches [Msg]es received.
class ConnectionHandler extends ChangeNotifier implements MsgVisitor<void, void> {
  GameList gameList = GameList();
  WizardModel? wizardModel;

  final ValueNotifier<ConnectionState> state = ValueNotifier(ConnectionState.idle);

  String _serverAddress = "wss://play.haumacher.de/wizard-game/ws";

  /// The players nickname as it was entered in the [LoginView].
  String? _nickName;

  /// The ID of the player in this app.
  String? playerId;

  /// ID of the currently joined game.
  String? get gameId => currentGame?.game.gameId;

  ObservableGame? currentGame;

  /// The [WebSocket] connection communicating with the game server.
  WebSocket? _socket;

  void Function()? _connectCallback;

  String get serverAddress => _serverAddress;

  void login(String serverAddress, String nickName) {
    _socket?.close();
    _socket = null;

    if (_serverAddress != serverAddress) {
      playerId = null;
    }

    _serverAddress = serverAddress;
    _nickName = nickName;

    var socket = WebSocket(_serverAddress);
    socket.onOpen.listen(_onOpen);
    socket.onMessage.listen(_onMessage);
    socket.onClose.listen(_onClose);
    _socket = socket;

    state.value = ConnectionState.connecting;
    notifyListeners();
  }

  void close() {
    _socket?.close();
    _socket = null;

    state.value = ConnectionState.idle;
    notifyListeners();
  }

  void _onOpen(Event evt) {
    state.value = ConnectionState.connected;
    notifyListeners();

    _connectCallback?.call();
    _connectCallback = null;

    sendCommand(Login(name: _nickName!, version: 3, locale: PlatformDispatcher.instance.locale.languageCode));
    notifyListeners();
  }

  void sendCommand(Cmd login) {
    var data = login.toString();
    if (kDebugMode) {
      print("< " + data);
    }
    _socket!.sendString(data);
  }

  void _onClose(Event evt) {
    _socket = null;
    state.value = playerId == null ? ConnectionState.idle : ConnectionState.disconnected;
    notifyListeners();
  }

  void _onMessage(MessageEvent evt) {
    String data = evt.data;
    if (kDebugMode) {
      print("> " + data);
    }
    var msg = Msg.fromString(data);
    msg?.visitMsg(this, null);
  }

  @override
  void visitWelcome(Welcome self, void arg) {
    playerId = self.playerId;
    state.value = ConnectionState.connected;
    sendCommand(ListGames());
    notifyListeners();
  }

  @override
  void visitListGamesResult(ListGamesResult self, void arg) {
    gameList.setGames(self.games);
    state.value = ConnectionState.searchingGame;
    notifyListeners();
  }

  @override
  void visitGameCreated(GameCreated self, void arg) {
    gameList.addGame(self.game!);
  }

  @override
  void visitGameDeleted(GameDeleted self, void arg) {
    gameList.removeGame(self.gameId);
  }

  @override
  void visitGameStarted(GameStarted self, void arg) {
    gameList.removeGame(self.game!.gameId);
    if (gameId == self.game?.gameId) {
      wizardModel = WizardModel(playerId!, currentGame!.game);
      state.value = ConnectionState.playing;
      notifyListeners();
    }
  }

  @override
  void visitJoinAnnounce(JoinAnnounce self, void arg) {
    ObservableGame? game = gameList.join(self.gameId, self.player!);
    if (playerId == self.player?.id && game != null) {
      state.value = ConnectionState.waitingForStart;
      currentGame = game;

      notifyListeners();
    }
  }

  @override
  void visitLeaveAnnounce(LeaveAnnounce self, void arg) {
    gameList.leave(self.gameId, self.playerId);
    if (playerId == self.playerId) {
      state.value = ConnectionState.searchingGame;

      currentGame = null;

      sendCommand(ListGames());
      notifyListeners();
    }
  }

  @override
  void visitAnnounce(Announce self, void arg) {
    wizardModel?.visitAnnounce(self, arg);
  }

  @override
  void visitFinishGame(FinishGame self, void arg) {
    wizardModel?.visitFinishGame(self, arg);
  }

  @override
  void visitFinishRound(FinishRound self, void arg) {
    wizardModel?.visitFinishRound(self, arg);
  }

  @override
  void visitFinishTurn(FinishTurn self, void arg) {
    wizardModel?.visitFinishTurn(self, arg);
  }

  @override
  void visitRequestBid(RequestBid self, void arg) {
    wizardModel?.visitRequestBid(self, arg);
  }

  @override
  void visitRequestLead(RequestLead self, void arg) {
    wizardModel?.visitRequestLead(self, arg);
  }

  @override
  void visitRequestTrumpSelection(RequestTrumpSelection self, void arg) {
    wizardModel?.visitRequestTrumpSelection(self, arg);
  }

  @override
  void visitStartBids(StartBids self, void arg) {
    wizardModel?.visitStartBids(self, arg);
  }

  @override
  void visitStartLead(StartLead self, void arg) {
    wizardModel?.visitStartLead(self, arg);
  }

  @override
  void visitStartRound(StartRound self, void arg) {
    wizardModel?.visitStartRound(self, arg);
  }

  @override
  void visitError(Error self, void arg) {
    // TODO: implement visitError
  }
}

/// Phase the current game is in.
enum WizardPhase {
  /// Initial state.
  idle,

  /// Waiting for more players to join.
  created,

  /// Players have their cards. The phase is about to move forward to [trumpSelection], or [bidding].
  cardsGiven,

  /// One of the players is about to select the trump color, because a wizard card was chosen as trum card.
  trumpSelection,

  /// Players are placing their bids.
  bidding,

  /// Players are placing cards on the table until all players have lead a card.
  leading,

  /// After all players have played a card, the winner of the current trick is displayed.
  trickConfirmation,

  /// After the last trick in a round the points that were won in that round are displayed.
  roundConfirmation,

  /// The game has ended and the final result is displayed.
  resultConfirmation
}

/// Client-side game state of a wizard game.
class WizardModel extends ChangeNotifier implements GameMsgVisitor<void, void>, GameCmdVisitor<void, String> {
  /// The ID of the player playing in this app.
  String playerId;

  /// The game we are currently playing.
  Game game;

  final ValueNotifier<WizardPhase> phase = ValueNotifier(WizardPhase.idle);

  late StartRound roundInfo;

  final ValueNotifier<bool> imActive = ValueNotifier(false);

  Suit? trump;

  int expectedBids = 0;

  /// The player that won the [currentTrick].
  Player? turnWinner;

  Set<String> pendingConfirmations = {};

  List<PlayerScore> result = [];

  final ValueNotifier<Player?> activePlayer = ValueNotifier(null);

  Map<String, int> playersBid = {};
  List<PlayedCard> currentTrick = [];

  WizardModel(this.playerId, this.game);

  void init(String newId) {
    playerId = newId;
  }

  @override
  void visitAnnounce(Announce self, void arg) {
    self.cmd!.visitGameCmd(this, self.playerId);
  }

  @override
  void visitStartRound(StartRound self, void arg) {
    roundInfo = self;
    
    setState(WizardPhase.cardsGiven);
  }

  @override
  void visitRequestTrumpSelection(RequestTrumpSelection self, void arg) {
    setActivePlayer(self.playerId);
    setState(WizardPhase.trumpSelection);
  }

  @override
  void visitSelectTrump(SelectTrump self, String playerId) {
    trump = self.trumpSuit;
  }

  @override
  void visitStartBids(StartBids self, void arg) {
    playersBid = {};
    currentTrick = [];
    expectedBids = 0;
    setState(WizardPhase.bidding);
  }

  @override
  void visitRequestBid(RequestBid self, void arg) {
    expectedBids = self.expected;
    setActivePlayer(self.playerId);
    notifyListeners();
  }

  @override
  void visitBid(Bid self, String playerId) {
    playersBid[playerId] = self.cnt;
    notifyListeners();
  }

  @override
  void visitStartLead(StartLead self, void arg) {
    setState(WizardPhase.leading);
  }

  @override
  void visitRequestLead(RequestLead self, void arg) {
    setActivePlayer(self.playerId);
    notifyListeners();
  }

  @override
  void visitLead(Lead self, String playerId) {
    currentTrick.add(PlayedCard(playerId: playerId, card: self.card!));
    notifyListeners();
  }

  @override
  void visitFinishTurn(FinishTurn self, void arg) {
    turnWinner = self.winner;
    initConfirmations();
    setState(WizardPhase.trickConfirmation);
  }

  @override
  void visitConfirmTrick(ConfirmTrick self, String playerId) {
    pendingConfirmations.remove(playerId);
    notifyListeners();
  }

  @override
  void visitFinishRound(FinishRound self, void arg) {
    initConfirmations();
    setState(WizardPhase.roundConfirmation);
  }

  @override
  void visitConfirmRound(ConfirmRound self, String playerId) {
    pendingConfirmations.remove(playerId);
    notifyListeners();
  }

  @override
  void visitFinishGame(FinishGame self, void arg) {
    result = self.scores;
    setState(WizardPhase.resultConfirmation);
  }

  void setActivePlayer(String activePlayerId) {
    activePlayer.value = roundInfo.players.where((p) => p.id == activePlayerId).first;
    imActive.value = playerId == activePlayerId;
  }

  void setState(WizardPhase newState) {
    phase.value = newState;
  }

  void initConfirmations() {
    pendingConfirmations = roundInfo.players.map((e) => e.id).toSet();
  }
}

/// Main entry point of the app.
class WizardApp extends StatelessWidget {
  const WizardApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Zauberer',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: HomePage()
    );
  }
}

/// Homepage view of the app.
class HomePage extends StatelessWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ValueListenableBuilder<ConnectionState>(
      valueListenable: connection.state,
      builder: (context, state, child) {
        if (kDebugMode) {
          print("Building HomePage: " + state.name);
        }

        switch (state) {
          case ConnectionState.idle:
          case ConnectionState.disconnected:
            return Scaffold(
                appBar: AppBar(
                  title: const Text("Zauberer online"),
                ),
                body:  Center(
                  child: ElevatedButton(
                    child: const Text("Login"),
                    onPressed: () {
                      var result = Navigator.push(
                          context,
                          MaterialPageRoute<ConnectionData>(
                              builder: (context) => LoginView()));
                      result.then((data) {
                        if (data != null) {
                          connection.login(data.serverAddress, data.nickName);
                        }
                      });
                    },
                  ),
                )
            );
          case ConnectionState.connecting:
            return Scaffold(
                appBar: AppBar(
                  title: const Text("Zauberer online"),
                ),
                body: const Center(child: Text("Connecting...")));
          case ConnectionState.connected:
            return Scaffold(
                appBar: AppBar(
                  title: const Text("Zauberer online"),
                ),
                body: const Center(child: Text("Logging in...")));
          case ConnectionState.searchingGame:
            return ChangeObserverWidget<GameList>(
                observable: connection.gameList,
                builder: (context, gameList) {
                  var openGames = gameList.openGames;
                  return Scaffold(
                      appBar: AppBar(title: const Text("Join a game")),
                      body: openGames.isEmpty
                          ? const Center(child: Text("No open games."))
                          : ListView(
                          children: openGames.values.map((g) => GameEntryWidget(g)).toList()),
                      floatingActionButton: FloatingActionButton(child: Icon(Icons.add), onPressed: () {
                        connection.sendCommand(CreateGame());
                      }));
                });
          case ConnectionState.waitingForStart:
            ObservableGame? game = connection.currentGame;
            return game == null ?
              const Center(child: Text("ERROR: Game not set."))  :
              ChangeObserverWidget<ObservableGame>(
                observable: game,
                builder: (context, game) {
                  if (kDebugMode) {
                    print("Building GameLobbyState");
                  }
                  var players = game.players;
                  var body = players.isEmpty ?
                    const Center(child: Text("No players in this game.")) :
                    ListView(
                      children: players.map((player) =>
                        DecoratedBox(
                          decoration: BoxDecoration(
                            border: Border.all(color: Colors.blue)),
                          child: Row(
                            mainAxisSize: MainAxisSize.max,
                            children: [
                              Text(player.name)]))).toList());

                  return Scaffold(
                      appBar: AppBar(
                        title: const Text("Waiting for players"),
                      ),
                      body: body,
                      floatingActionButton:
                      FloatingActionButton.extended(
                          label: const Text("Start"),
                          icon: const Icon(Icons.play_arrow),
                          onPressed: () => connection.sendCommand(StartGame(gameId: game.game.gameId)),
                      ));
                });
          case ConnectionState.playing:
            return WizardWidget();
          default:
            return Center(child: Text("ERROR: " + state.name));
        }
      }
    );
  }
}

/// A single entry in the [GameListWidget] that displays the players that have already joined the game.
class GameEntryWidget extends StatefulWidget {
  final ObservableGame game;

  const GameEntryWidget(this.game, {Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return GameEntryState();
  }
}

/// State of [GameEntryWidget] painting its contents whenever the players of a game change.
class GameEntryState extends ChangeNotifierState<GameEntryWidget, ObservableGame> {

  @override
  ObservableGame fetchObservable(GameEntryWidget widget) => widget.game;

  @override
  Widget build(BuildContext context) {
    return DecoratedBox(
        decoration: BoxDecoration(border: Border.all(color: Colors.blue)),
        child: Row(mainAxisSize: MainAxisSize.max, children: [
          Text(observable.players.map((e) => e.name).join(", ")),
          JoinButton(observable.game.gameId)
        ],));
  }
}

/// Button in a [GameEntryState] that joins the displayed game.
class JoinButton extends StatelessWidget {
  /// The [Game] to join through this button.
  final String gameId;

  const JoinButton(this.gameId, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return IconButton(
        onPressed: () => connection.sendCommand(JoinGame(gameId: gameId)),
        icon: const Icon(Icons.keyboard_arrow_right));
  }
}

class WizardWidget extends StatelessWidget {
  const WizardWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text("Wizards in action"),
        ),
        body: createBody(context));
  }

  Widget createBody(BuildContext context) {
    return ValueListenableBuilder<WizardPhase>(
      valueListenable: connection.wizardModel!.phase,
      builder: (context, phase, child) {
        if (kDebugMode) {
          print("Building WizardWidget: " + phase.name);
        }

        switch (phase) {
          case WizardPhase.idle:
          case WizardPhase.created:
            return const Center(child: Text("Waiting for cards..."));
          case WizardPhase.cardsGiven:
            return const Center(child: Text("Waiting for bid request..."));

          case WizardPhase.trumpSelection:
          case WizardPhase.bidding:
            return ValueListenableBuilder<bool>(
              valueListenable: connection.wizardModel!.imActive,
              builder: (context, imActive, child) {
                return imActive ?
                  MyBidView(connection.wizardModel!.roundInfo.round) :
                  const WaitingForBidView();
              },
            );
          case WizardPhase.leading:
          case WizardPhase.trickConfirmation:
          case WizardPhase.roundConfirmation:
          case WizardPhase.resultConfirmation:

          default:
            return Center(child: Text("ERROR: " + phase.name));
        }
      });
  }
}

class WaitingForBidView extends StatelessWidget {
  const WaitingForBidView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ValueListenableBuilder<Player?>(
      valueListenable: connection.wizardModel!.activePlayer,
      builder: (context, player, child) {
        return Center(child: Text("Waiting for " + player!.name + "'s bid."));
      });
  }
}

class MyBidView extends StatelessWidget {
  final int round;
  const MyBidView(this.round, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text("Dein Gebot bitte!"),
        ),
        body:
          Center(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text("Place your bid"),
                Padding(
                    padding: EdgeInsets.all(15),
                    child: Wrap(
                        direction: Axis.horizontal,
                        children: options()))])));
  }

  List<Widget> options() {
    List<Widget> result = [];
    for (var x = 0; x <= round ; x++) {
      result.add(
        Padding(
          padding: EdgeInsets.all(5),
          child:
            ElevatedButton(
              child: Text(
                x.toString(),
                style: TextStyle(fontSize: 20),
              ),
              style: ElevatedButton.styleFrom(
                fixedSize: const Size(60, 60),
                shape: const CircleBorder(),
              ),
              onPressed: () {
                connection.sendCommand(Bid(cnt: x));
              }
            )));
    }
    return result;
  }
}

class TrickView extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Place your cards")),
      body: Center(
          child: CardView(msg.Card(suit: Suit.heart, value: Value.c13))
      ),
    );
  }

}

class CardView extends StatelessWidget {
  final msg.Card card;

  const CardView(this.card, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
        decoration: BoxDecoration(
            border: Border.all(
              color: Colors.black,
              width: 2,
            ),
            borderRadius: BorderRadius.all(Radius.circular(8))
        ),
        child:
          Padding(padding: EdgeInsets.symmetric(horizontal: 10, vertical: 8),
            child:
              Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Padding(padding: EdgeInsets.fromLTRB(0, 0, 0, 8),
                      child: Text("13", style: TextStyle(fontSize: 35, fontWeight: FontWeight.bold))),
                    CustomPaint(painter: SuitDiamond(), size: Size(70, 70))
                  ]
              )
          )
    );
  }
}

class SuitHeart extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    Paint paint = Paint();
    paint.color = Color(0xffdd2c0b);

    ScaledPath path = ScaledPath(size);
    path.moveTo(24.83093,49.946898);
    path.cubicTo(20.838961,40.169796, 8.5336001,27.700032, 3.6375781,20.368633);
    path.cubicTo(2.4137414,18.304694, 1.7653807,15.950477, 1.7599623,13.55099);
    path.cubicTo(1.7602127,6.1355301, 7.7771213,-0.14851479, 15.187532,0.12511163);
    path.cubicTo(19.464577,0.28309523, 24.09139,5.1826486, 25.174548,7.3794898);
    path.cubicTo(26.087272,5.8642246, 29.183628,0.43681997, 34.556047,0.12511163);
    path.cubicTo(41.958399,-0.30436895, 47.981659,6.136206, 47.981907,13.55099);
    path.relativeCubicTo(-0.0036,3.259597, -1.193393,6.406448, -3.346673,8.853537);
    path.cubicTo(35.357321,32.568454, 29.93524,40.392439, 24.83093,49.946898);
    path.close();

    canvas.drawPath(path.path, paint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) {
    return true;
  }
}

class SuitClub extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    Paint paint = Paint();
    paint.color = Color(0xff002ccd);

    ScaledPath path = ScaledPath(size);
    path.moveTo(24.818217,0.10278802);
    path.relativeCubicTo(-2.611116,0, -4.763189,1.22415728, -6.413414,2.46202558);
    path.relativeCubicTo(-2.258243,1.7228674, -3.68206,4.0893193, -3.763869,7.4859124);
    path.relativeCubicTo(-0.11074,4.597438, 1.463891,6.662971, 3.804339,8.437001);
    path.cubicTo(13.942045,14.60858, 8.7526072,14.586656, 5.2326169,16.791962);
    path.cubicTo(2.0930668,18.758917, 0.35772586,22.586214, 0.43402016,25.643117);
    path.cubicTo(0.58692202,31.769708, 6.3028203,36.842906, 13.015325,36.370813);
    path.relativeCubicTo(4.113289,-0.0014, 7.926135,-1.716747, 10.263617,-4.536875);
    path.relativeCubicTo(-0.260171,2.78135, -0.495906,5.539687, -0.956477,8.293995);
    path.relativeCubicTo(-0.650745,3.024732, -1.461633,6.010046, -2.474169,8.662294);
    path.relativeCubicTo(-0.169166,0.387983, -0.346725,0.7764, -0.535582,1.165584);
    path.relativeCubicTo(3.326956,-0.476144, 7.829552,-0.479121, 11.054171,0);
    path.relativeCubicTo(-0.192139,-0.395974, -0.373402,-0.791125, -0.545017,-1.185821);
    path.relativeCubicTo(-0.912975,-2.384058, -1.672958,-5.037653, -2.306891,-7.744932);
    path.relativeCubicTo(-0.572472,-3.073821, -0.829204,-6.147927, -1.119713,-9.251826);
    path.relativeCubicTo(2.332882,2.855863, 6.169753,4.596346, 10.312187,4.597581);
    path.relativeCubicTo(6.712491,0.472098, 12.428316,-4.601098, 12.581296,-10.727696);
    path.relativeCubicTo(0.07631,-3.056911, -1.659026,-6.884187, -4.798588,-8.851155);
    path.relativeCubicTo(-3.519983,-2.205293, -8.709399,-2.183403, -13.212661,1.695765);
    path.relativeCubicTo(2.340466,-1.774051, 3.915024,-3.839555, 3.804337,-8.437001);
    path.cubicTo(35.000019,6.6541459, 33.576232,4.287694, 31.317968,2.5648136);
    path.cubicTo(29.065436,1.1622488, 27.429337,0.10278802, 24.818217,0.10278802);
    path.close();

    canvas.drawPath(path.path, paint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) {
    return true;
  }
}

class SuitDiamond extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    Paint paint = Paint();
    paint.color = Color(0xffddbb0b);

    ScaledPath path = ScaledPath(size);
    path.moveTo(24.917204,0.07341574);
    path.cubicTo(30.624333,8.9487281, 36.608578,17.761107, 49.832074,24.929874);
    path.cubicTo(40.384097,30.811458, 31.705253,38.296387, 24.975641,49.84474);
    path.cubicTo(18.777163,39.870015, 11.184989,31.012944, 0.06077093,24.98828);
    path.cubicTo(10.700803,18.537719, 18.986282,10.232773, 24.917204,0.07341574);
    path.close();

    canvas.drawPath(path.path, paint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) {
    return true;
  }
}

class ScaledPath {
  Path path = Path();
  
  late double dx, dy, f;

  ScaledPath(Size size) {
    var dim = min(size.width, size.height);
    dx = (size.width - dim) / 2;
    dy = (size.height - dim) / 2;
    f = dim / 50;
  }

  void moveTo(double x, double y) {
    path.moveTo(tx(x), ty(y));
  }

  void cubicTo(double x1, double y1, double x2, double y2, double x3, double y3) {
    path.cubicTo(tx(x1), ty(y1), tx(x2), ty(y2), tx(x3), ty(y3));
  }

  void relativeCubicTo(double x1, double y1, double x2, double y2, double x3, double y3) {
    path.relativeCubicTo(rx(x1), ry(y1), rx(x2), ry(y2), rx(x3), ry(y3));
  }

  void close() {
    path.close();
  }

  double tx(x) => dx + f * x;
  double ty(y) => dy + f * y;
  double rx(x) => f * x;
  double ry(y) => f * y;
}

/// Route displaying a form requesting a nick name and a server address.
/// Returns a [ConnectionData] object back to the opener.
class LoginView extends StatefulWidget {
  LoginView({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return LoginViewState();
  }
}

class LoginViewState extends State<LoginView> {
  final GlobalKey<FormState> _formKey = GlobalKey(debugLabel: "loginState");
  final GlobalKey<FormFieldState<String>> _nickName = GlobalKey();
  final GlobalKey<FormFieldState<String>> _serverAddress = GlobalKey();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Login to Wizard server"),
      ),
      body: Form(
        key: _formKey,
        child: Padding(
          padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 16),
          child: Column(
            children: [
              TextFormField(
                key: _nickName,
                decoration: const InputDecoration(hintText: "Nick name"),
                validator: (String? value) {
                  if (value == null || value.isEmpty) {
                    return "Nick name must not be empty.";
                  }
                },
              ),
              TextFormField(
                key: _serverAddress,
                decoration: const InputDecoration(
                  hintText: "Server URL",
                  labelText: "Wizard server",
                ),
                initialValue: connection.serverAddress,
                validator: (String? value) {
                  if (value == null || value.isEmpty) {
                    return "Server address must not be empty.";
                  }
                },
              ),
              Padding(
                padding:
                    const EdgeInsets.symmetric(vertical: 16, horizontal: 16),
                child: ElevatedButton(
                  child: const Text("Connect"),
                  onPressed: () {
                    if (_formKey.currentState!.validate()) {
                      Navigator.pop(
                        context,
                        ConnectionData(_nickName.currentState!.value!,
                          _serverAddress.currentState!.value!));
                    }
                  },
                )),
            ],
          ),
        ),
      ),
    );
  }
}

/// Data collected by [LoginView].
class ConnectionData {
  String nickName;
  String serverAddress;

  ConnectionData(this.nickName, this.serverAddress);
}

class ChangeObserverWidget<S extends ChangeNotifier> extends StatefulWidget {
  final S observable;
  final Widget Function(BuildContext, S) builder;

  const ChangeObserverWidget({Key? key, required this.observable, required this.builder}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return _ChangeObserverState<ChangeObserverWidget<S>, S>();
  }

}

class _ChangeObserverState<T extends ChangeObserverWidget<S>, S extends ChangeNotifier> extends State<T> {
  late S observable;
  late Widget Function(BuildContext, S) builder;

  @override
  void initState() {
    super.initState();

    observable = widget.observable;
    builder = widget.builder;

    observable.addListener(_onChange);
  }

  @override
  void dispose() {
    observable.removeListener(_onChange);
    super.dispose();
  }

  void _onChange() {
    // Fake an update to this state in response to a listener notification.
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return builder(context, observable);
  }
}

abstract class ChangeNotifierState<T extends StatefulWidget, S extends ChangeNotifier> extends State<T> {
  late S _observable;

  S get observable => _observable;

  @override
  void initState() {
    super.initState();

    _observable = fetchObservable(widget);
    _observable.addListener(_onChange);
  }
  
  S fetchObservable(T widget);

  @override
  void dispose() {
    _observable.removeListener(_onChange);
    super.dispose();
  }

  void _onChange() {
    // Fake an update to this state in response to a listener notification.
    setState(() {});
  }

}

