import 'dart:io';
import 'dart:math';
import 'dart:core';
import 'dart:ui';

import 'package:web_socket_channel/web_socket_channel.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_ui/msg.dart';
import 'package:flutter_ui/msg.dart' as msg;
import 'package:flutter_ui/svg.dart';

void main() {
  runApp(WizardApp());
  // showTrickTestPage();
}

void showTrickTestPage() {
  runApp(MaterialApp(
    title: 'Zauberer',
    theme: ThemeData(
      primarySwatch: Colors.blue,
    ),
    home: Scaffold(
      appBar: AppBar(title: const Text("Place your cards")),
      body: ExpandDisplay(
        children: [
          Padding(padding: const EdgeInsets.all(16),
            child:
            PlayerStateView(
              players: [
                Player(id: "1", name: "Haui"),
                Player(id: "2", name: "Egon"),
                Player(id: "3", name: "Der große Zauberer"),
                Player(id: "4", name: "Player A"),
                Player(id: "5", name: "Player B"),
                Player(id: "6", name: "Player C"),
              ],
              activePlayerIds: ValueNotifier(const {"1", "2", "3", "6"}),
              state: {
                "1": PlayerInfo(points: 250, bid: 4, tricks: 3),
                "2": PlayerInfo(points: 20, bid: 2, tricks: 5),
                "3": PlayerInfo(points: 180, bid: 0, tricks: 0),
                "4": PlayerInfo(points: 20, bid: 2, tricks: 0),
                "5": PlayerInfo(points: 20, bid: 0, tricks: 4),
                "6": PlayerInfo(points: 20, bid: 4, tricks: 4),
              }),
          ),
          Expanded(child:
            Center(child:
              TrickView([
                TrickCard(msg.Card(value: Value.c10, suit: Suit.heart), Player(name: "Player A")),
                TrickCard(msg.Card(value: Value.c1, suit: Suit.club), Player(name: "Player B")),
                TrickCard(msg.Card(value: Value.c13, suit: Suit.spade), Player(name: "Player C")),
                TrickCard(msg.Card(value: Value.c9, suit: Suit.diamond), Player(name: "Egon")),
                TrickCard(msg.Card(value: Value.z, suit: null), Player(name: "Bert")),
                TrickCard(msg.Card(value: Value.n, suit: null), Player(name: "Der große Zauberer")),
              ])
            )),
          Padding(padding: const EdgeInsets.all(16),
            child: CardListView([
              msg.Card(value: Value.z, suit: null),
              msg.Card(value: Value.c13, suit: Suit.club),
              msg.Card(value: Value.c13, suit: Suit.spade),
              msg.Card(value: Value.c13, suit: Suit.spade),
              msg.Card(value: Value.c13, suit: Suit.spade),
              msg.Card(value: Value.c13, suit: Suit.spade),
              msg.Card(value: Value.c13, suit: Suit.spade),
              msg.Card(value: Value.c13, suit: Suit.spade),
              msg.Card(value: Value.c13, suit: Suit.spade),
              msg.Card(value: Value.c13, suit: Suit.spade),
              msg.Card(value: Value.c13, suit: Suit.spade),
              msg.Card(value: Value.c13, suit: Suit.spade),
              msg.Card(value: Value.c13, suit: Suit.heart),
              msg.Card(value: Value.c10, suit: Suit.heart),
              msg.Card(value: Value.c13, suit: Suit.spade),
              msg.Card(value: Value.c9, suit: Suit.diamond),
              msg.Card(value: Value.c8, suit: Suit.diamond),
              msg.Card(value: Value.c2, suit: Suit.diamond),
              msg.Card(value: Value.c1, suit: Suit.diamond),
              msg.Card(value: Value.n, suit: null),
              ])),
        ]
      ))));
}

class ExpandDisplay extends StatelessWidget {
  final List<Widget> children;
  const ExpandDisplay({Key? key, required this.children}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        return SingleChildScrollView(
          scrollDirection: Axis.vertical,
          child: ConstrainedBox(
            constraints: BoxConstraints(minWidth: constraints.maxWidth, minHeight: constraints.maxHeight),
            child: IntrinsicHeight(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: children)
            )));
    });
  }
}

/// Observable list of open games displayed in the [GameListWidget].
class GameList extends ChangeNotifier {

  Map<String, ObservableGame> openGames = {};

  ObservableGame addGame(Game newGame) {
    var result = ObservableGame(newGame);
    openGames[newGame.gameId] = result;
    notifyListeners();
    return result;
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

  /// The [WebSocketChannel] communicating with the game server.
  WebSocketChannel? _socket;

  void Function()? _connectCallback;

  String get serverAddress => _serverAddress;

  void login(String serverAddress, String nickName) {
    _socket?.sink.close();
    _socket = null;

    if (_serverAddress != serverAddress) {
      playerId = null;
    }

    _serverAddress = serverAddress;
    _nickName = nickName;

    var socket = WebSocketChannel.connect(Uri.parse(_serverAddress));
    socket.stream.listen(_onMessage, onDone: _onClose);
    // socket.onOpen.listen(_onOpen);
    _socket = socket;

    state.value = ConnectionState.connecting;
    _onOpen();
  }

  void close() {
    _socket?.sink.close();
    _socket = null;

    state.value = ConnectionState.idle;
    notifyListeners();
  }

  void _onOpen() {
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
    _socket!.sink.add(data);
  }

  void _onClose() {
    _socket = null;
    state.value = playerId == null ? ConnectionState.idle : ConnectionState.disconnected;
    notifyListeners();
  }

  void _onMessage(data) {
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
    
    // A join announce is sent later on, so it is not required to set the 
    // current game, if the game's owner is the current player.
  }

  @override
  void visitGameDeleted(GameDeleted self, void arg) {
    gameList.removeGame(self.gameId);

    if (gameId == self.gameId) {
      leaveGame();
    }
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
      enterGame(game);
    }
  }

  void enterGame(ObservableGame game) {
    state.value = ConnectionState.waitingForStart;
    currentGame = game;
    
    notifyListeners();
  }

  @override
  void visitLeaveAnnounce(LeaveAnnounce self, void arg) {
    gameList.leave(self.gameId, self.playerId);
    if (playerId == self.playerId) {
      leaveGame();
    }
  }

  void leaveGame() {
    state.value = ConnectionState.searchingGame;
    currentGame = null;
    
    sendCommand(ListGames());
    notifyListeners();
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

  StartRound? roundInfo;

  final ValueNotifier<bool> imActive = ValueNotifier(false);

  Suit? trump;

  int expectedBids = 0;

  /// The player that won the [currentTrick].
  Player? turnWinner;

  Set<String> pendingConfirmations = {};

  List<PlayerScore> result = [];

  final ValueNotifier<Set<String>> activePlayerIds = ValueNotifier({});

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
    activePlayerIds.value = {activePlayerId};
    imActive.value = playerId == activePlayerId;
  }

  void setState(WizardPhase newState) {
    phase.value = newState;
  }

  void initConfirmations() {
    pendingConfirmations = roundInfo!.players.map((e) => e.id).toSet();
  }

  Player player(String playerId) {
    return roundInfo!.players.where((element) => element.id == playerId).first;
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
      home: const HomePage()
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
                      var result = Navigator.push(context,
                          MaterialPageRoute<ConnectionData>(
                              builder: (context) => const LoginView()));
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
                      body: openGames.isEmpty ?
                          const Center(child: Text("No open games.")) :
                          ListView(
                            children: openGames.values.map((g) =>
                              Padding(padding: const EdgeInsets.fromLTRB(5, 5, 5, 0),
                                child: GameEntryWidget(g))
                            ).toList()),
                      floatingActionButton: FloatingActionButton(
                        child: const Icon(Icons.add),
                        onPressed: () {
                          pushGameView(context);
                          connection.sendCommand(CreateGame());
                        }));
                });
          case ConnectionState.waitingForStart:
            return showWaitingForStart();
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
      decoration: BoxDecoration(
        color: Colors.cyan,
        borderRadius: BorderRadius.circular(5)),

      child:
        Padding(padding: const EdgeInsets.fromLTRB(10, 5, 5, 5),
          child: Row(
            mainAxisSize: MainAxisSize.max,
            children: [
              Expanded(child:
                Text(observable.players.map((e) => e.name).join(", "))),
              ElevatedButton(
                style: ElevatedButton.styleFrom(
                  fixedSize: const Size(40, 40),
                  shape: const CircleBorder(),
                ),
                onPressed: () {
                  var gameId = observable.game.gameId;
                  pushGameView(context);
                  connection.sendCommand(JoinGame(gameId: gameId));
                },
                child: const Icon(Icons.keyboard_arrow_right))
            ])));
  }
}

void pushGameView(BuildContext context) {
  var routeToGame = MaterialPageRoute(builder: (context) => const PlayingView());
  routeToGame.popped.then((_) {
    var gameId = connection.gameId;
    if (gameId != null) {
      connection.sendCommand(LeaveGame(gameId: gameId));
    }
  });
  Navigator.push(context, routeToGame);
}

class PlayingView extends StatelessWidget {
  const PlayingView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ValueListenableBuilder<ConnectionState>(
      valueListenable: connection.state,
      builder: (context, state, child) {
        if (kDebugMode) {
          print("Building PlayingView: " + state.name);
        }

        switch (state) {
          case ConnectionState.searchingGame:
            return showWaitingForStart();
          case ConnectionState.waitingForStart:
            ObservableGame? game = connection.currentGame;
            if (game == null) return showWaitingForStart();
            return ChangeObserverWidget<ObservableGame>(observable: game,
                builder: (context, game) {
                  if (kDebugMode) {
                    print("Building GameLobbyState");
                  }
                  var players = game.players;

                  return Scaffold(
                    appBar: AppBar(
                      title: const Text("Waiting for players"),
                    ),
                    body: players.isEmpty ?
                    const Center(child: Text("No players in this game.")) :
                    ListView(
                      children: [
                        for (var n = 0; n < players.length; n++)
                          Padding(padding: const EdgeInsets.fromLTRB(5, 5, 5, 0),
                            child: DecoratedBox(
                              decoration: const BoxDecoration(
                                color: Colors.orangeAccent,
                                borderRadius: BorderRadius.all(Radius.circular(5)),
                              ),
                              child: Row(
                                mainAxisSize: MainAxisSize.max,
                                children: [
                                  Padding(padding: const EdgeInsets.fromLTRB(8, 8, 8, 8),
                                      child:
                                      Text((n + 1).toString() + ". " + players[n].name))
                                ])))
                      ]
                    ),
                    floatingActionButton:
                    FloatingActionButton.extended(
                      label: const Text("Start"),
                      icon: const Icon(Icons.play_arrow),
                      onPressed: () => connection.sendCommand(StartGame(gameId: game.game.gameId)),
                    ));
                });
          case ConnectionState.playing:
            return const WizardWidget();
          default:
            return Text("Error: " + state.name);
        }
      });
  }
}

Scaffold showWaitingForStart() {
  return Scaffold(
      appBar: AppBar(
        title: const Text("Waiting for players"),
      ),
      body: const Center(child: Text("Joining game...")));
}

class WizardWidget extends StatelessWidget {
  const WizardWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ValueListenableBuilder<WizardPhase>(
      valueListenable: connection.wizardModel!.phase,
      builder: (context, phase, child) {
        if (kDebugMode) {
          print("Building WizardWidget: " + phase.name);
        }

        var roundInfo = connection.wizardModel!.roundInfo;
        return Scaffold(
          appBar: AppBar(
            title: roundInfo == null ?
              const Text("Waiting for start...") :
              Text("Round " + roundInfo.round.toString() + " of " + roundInfo.maxRound.toString()),
          ),
          body: createBody(context, phase));
      });
  }

  Widget createBody(BuildContext context, WizardPhase phase) {
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
              MyBidView(connection.wizardModel!.roundInfo!.round) :
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
  }
}

class WaitingForBidView extends StatelessWidget {
  const WaitingForBidView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var wizardModel = connection.wizardModel;
    
    return ValueListenableBuilder<Set<String>>(
      valueListenable: wizardModel!.activePlayerIds,
      builder: (context, playerIds, child) {
        return Center(
          child: Text(
            "Waiting for " + wizardModel.player(playerIds.first).name + "'s bid."));
      });
  }
}

class MyBidView extends StatelessWidget {
  final int round;
  const MyBidView(this.round, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          const Text("Place your bid"),
          Padding(
              padding: const EdgeInsets.all(15),
              child: Wrap(
                  direction: Axis.horizontal,
                  children: options()))]));
  }

  List<Widget> options() {
    List<Widget> result = [];
    for (var x = 0; x <= round ; x++) {
      result.add(
        Padding(
          padding: const EdgeInsets.all(5),
          child:
            ElevatedButton(
              child: Text(
                x.toString(),
                style: const TextStyle(fontSize: 20),
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

class TrickCard {
  msg.Card card;
  Player player;

  TrickCard(this.card, this.player);
}

class TrickView extends StatelessWidget {

  final List<TrickCard> cards;

  const TrickView(this.cards, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Wrap(
      spacing: 5,
      runSpacing: 10,
      children:
        cards.map((card) =>
          Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              CardView(card.card),
              Padding(padding: const EdgeInsets.fromLTRB(0, 5, 0, 0),
                child: Text(card.player.name),
              )
          ])).toList()
  );
  }
}

class CardListView extends StatelessWidget {

  final List<msg.Card> cards;

  const CardListView(this.cards, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Wrap(
      spacing: 5,
      runSpacing: 5,
      children:
        cards.map((card) => CardView(card)).toList()
    );
  }
}

class PlayerStateView extends StatelessWidget {
  final List<Player> players;
  final ValueNotifier<Set<String>> activePlayerIds;
  final Map<String, PlayerInfo> state;

  const PlayerStateView({required this.players, required this.activePlayerIds, required this.state, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Wrap(
      direction: Axis.horizontal,
      alignment: WrapAlignment.center,
      spacing: 5,
      runSpacing: 10,
      children:
        players.map(playerStateView).toList(),
    );
  }

  Widget playerStateView(Player player) {
    var info = state[player.id]!;

    return ChangeObserverWidget<ValueNotifier<Set<String>>>(
      observable: activePlayerIds,
      builder: (context, activePlayers) {
        return DecoratedBox(
          decoration: activePlayers.value.contains(player.id) ?
            BoxDecoration(color: Colors.amber, borderRadius: BorderRadius.circular(10)) :
            const BoxDecoration(),
          child: Padding(
            padding: const EdgeInsets.all(8),
            child:
            Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Padding(
                  padding: const EdgeInsets.fromLTRB(0, 0, 0, 5),
                  child:
                  Text(player.name + " (" + info.points.toString() + ")")),
                trickView(info)
              ])
          ));
      });
  }

  Widget trickView(PlayerInfo info) {
    var markers = max(info.bid, info.tricks);
    if (markers == 0) {
      return const Text("--");
    }
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        for (var n = 0; n < markers; n++)
          Padding(padding: EdgeInsets.fromLTRB(n > 0 && n % 3 == 0 ? 2 : 0, 0, 0, 0),
            child:
          DecoratedBox(
            decoration: BoxDecoration(
              border: Border(
                left: n % 3 > 0 ? const BorderSide(style: BorderStyle.none) : const BorderSide(width: 2),
                top: const BorderSide(width: 2),
                right: const BorderSide(width: 2),
                bottom: const BorderSide(width: 2),
              ),
              color: n >= info.bid ? Colors.red : n >= info.tricks ? Colors.white : Colors.green,
            ),
            child: SizedBox(width: n % 3 > 0 ? 8 : 10, height: 16),
          ))
      ],
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
            borderRadius: const BorderRadius.all(Radius.circular(8))
        ),
        child:
          Padding(padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 8),
            child:
              Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Padding(padding: const EdgeInsets.fromLTRB(0, 0, 0, 8),
                      child:
                        Text(valueText(card.value),
                          style: const TextStyle(fontSize: 25, fontWeight: FontWeight.bold))),
                    CustomPaint(painter: suitPainter, size: const Size(50, 50))
                  ]
              )
          )
    );
  }

  CustomPainter get suitPainter {
    switch (card.suit) {
      case null: return valuePainter(card.value);
      case Suit.diamond: return SuitDiamond();
      case Suit.heart: return SuitHeart();
      case Suit.spade: return SuitSpade();
      case Suit.club: return SuitClub();
    }
  }

  static String valueText(Value value) {
    switch (value) {
      case Value.z: return "Z";
      case Value.n: return "N";
      default: return value.name.substring(1);
    }
  }

  static CustomPainter valuePainter(Value value) {
    switch (value) {
      case Value.z: return SuitWizard();
      case Value.n: return SuitJester();
      default: return SuitNone();
    }
  }
}

class SuitNone extends SVGPathPainter {
  static final Trace trace = Trace.fromString("");
  SuitNone() : super(colorPaint(Colors.black), trace);
}

class SuitClub extends SVGPathPainter {
  static final Trace trace = Trace.fromString("m 24.818217,0.10278802 c -2.611116,0 -4.763189,1.22415728 -6.413414,2.46202558 -2.258243,1.7228674 -3.68206,4.0893193 -3.763869,7.4859124 -0.11074,4.597438 1.463891,6.662971 3.804339,8.437001 C 13.942045,14.60858 8.7526072,14.586656 5.2326169,16.791962 2.0930668,18.758917 0.35772586,22.586214 0.43402016,25.643117 0.58692202,31.769708 6.3028203,36.842906 13.015325,36.370813 c 4.113289,-0.0014 7.926135,-1.716747 10.263617,-4.536875 -0.260171,2.78135 -0.495906,5.539687 -0.956477,8.293995 -0.650745,3.024732 -1.461633,6.010046 -2.474169,8.662294 -0.169166,0.387983 -0.346725,0.7764 -0.535582,1.165584 3.326956,-0.476144 7.829552,-0.479121 11.054171,0 -0.192139,-0.395974 -0.373402,-0.791125 -0.545017,-1.185821 -0.912975,-2.384058 -1.672958,-5.037653 -2.306891,-7.744932 -0.572472,-3.073821 -0.829204,-6.147927 -1.119713,-9.251826 2.332882,2.855863 6.169753,4.596346 10.312187,4.597581 6.712491,0.472098 12.428316,-4.601098 12.581296,-10.727696 0.07631,-3.056911 -1.659026,-6.884187 -4.798588,-8.851155 -3.519983,-2.205293 -8.709399,-2.183403 -13.212661,1.695765 2.340466,-1.774051 3.915024,-3.839555 3.804337,-8.437001 C 35.000019,6.6541459 33.576232,4.287694 31.317968,2.5648136 29.065436,1.1622488 27.429337,0.10278802 24.818217,0.10278802 Z");
  SuitClub() : super(colorPaint(const Color(0xff002ccd)), trace);
}

class SuitSpade extends SVGPathPainter {
  static final Trace trace = Trace.fromString("M 25.151149,0.09518411 C 20.559348,8.0558012 10.601109,17.121738 6.9217582,22.629778 5.9484207,24.202221 5.4333289,25.994733 5.4290154,27.822844 5.4292152,33.47241 10.213921,38.260526 16.107538,38.05207 c 2.226898,-0.07867 5.93154,-1.70326 7.528073,-3.287248 -0.737791,5.679864 -2.358523,11.375024 -5.944135,15.324382 H 32.98233 c -3.45529,-3.99645 -4.949857,-9.535254 -5.937423,-15.296219 1.275543,1.49423 4.58553,3.113055 7.212891,3.259085 5.887206,0.327219 10.678325,-4.580182 10.678517,-10.229226 -0.0029,-2.483408 -0.94971,-4.880474 -2.662253,-6.744814 C 35.066865,12.140854 29.290188,6.8216619 25.151149,0.09518411 Z");
  SuitSpade() : super(colorPaint(const Color(0xff00810b)), trace);
}

class SuitHeart extends SVGPathPainter {
  static final Trace trace = Trace.fromString("M 24.83093,49.946898 C 20.838961,40.169796 8.5336001,27.700032 3.6375781,20.368633 2.4137414,18.304694 1.7653807,15.950477 1.7599623,13.55099 1.7602127,6.1355301 7.7771213,-0.14851479 15.187532,0.12511163 19.464577,0.28309523 24.09139,5.1826486 25.174548,7.3794898 26.087272,5.8642246 29.183628,0.43681997 34.556047,0.12511163 41.958399,-0.30436895 47.981659,6.136206 47.981907,13.55099 c -0.0036,3.259597 -1.193393,6.406448 -3.346673,8.853537 C 35.357321,32.568454 29.93524,40.392439 24.83093,49.946898 Z");
  SuitHeart() : super(colorPaint(const Color(0xffdd2c0b)), trace);
}

class SuitDiamond extends SVGPathPainter {
  static final Trace trace = Trace.fromString("M 24.917204,0.07341574 C 30.624333,8.9487281 36.608578,17.761107 49.832074,24.929874 40.384097,30.811458 31.705253,38.296387 24.975641,49.84474 18.777163,39.870015 11.184989,31.012944 0.06077093,24.98828 10.700803,18.537719 18.986282,10.232773 24.917204,0.07341574 Z");
  SuitDiamond() : super(colorPaint(const Color(0xffddbb0b)), trace);
}

class SuitWizard extends SVGPathPainter {
  static final Trace trace = Trace.fromString("M 43.739075,0.80459084 30.278431,23.510008 26.414548,3.6203742 10.518686,27.74013 4.4988124,23.006065 C 4.3088064,22.834658 4.1091493,22.672499 3.8883804,22.526803 L 3.3870511,26.058225 0.1192992,49.35716 19.255645,38.78893 22.375312,37.062841 16.14113,32.161213 22.748061,22.137165 26.857443,43.269754 49.868051,4.4372995 Z");
  SuitWizard() : super(colorPaint(const Color(0xff000000)), trace);
}

class SuitJester extends SVGPathPainter {
  static final Trace trace = Trace.fromString("M 49.889882,12.497489 A 11.655525,11.655525 0 0 1 38.234354,24.153041 11.655525,11.655525 0 0 1 26.578826,12.497489 11.655525,11.655525 0 0 1 38.234354,0.84198583 11.655525,11.655525 0 0 1 49.889882,12.497489 Z M 23.22196,25.458455 a 8.2054899,8.2054899 0 0 1 -8.20549,8.20549 8.2054899,8.2054899 0 0 1 -8.2054896,-8.20549 8.2054899,8.2054899 0 0 1 8.2054896,-8.20549 8.2054899,8.2054899 0 0 1 8.20549,8.20549 z M 10.727322,43.827567 A 5.3149192,5.3149192 0 0 1 5.4123939,49.142493 5.3149192,5.3149192 0 0 1 0.09746859,43.827567 5.3149192,5.3149192 0 0 1 5.4123939,38.512642 5.3149192,5.3149192 0 0 1 10.727322,43.827567 Z");
  SuitJester() : super(colorPaint(const Color(0xffa6a6ae)), trace);
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
  const LoginView({Key? key}) : super(key: key);

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

