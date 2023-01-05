import 'dart:math';
import 'dart:core';
import 'dart:ui';

import 'package:flutter_gen/gen_l10n/app_localizations.dart';

import 'package:web_socket_channel/web_socket_channel.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_ui/msg.dart';
import 'package:flutter_ui/msg.dart' as msg;
import 'package:flutter_ui/svg.dart';

/// The protocol version supported by this client app.
const int protocolVersion = 6;

void main() {
    runApp(const WizardApp());
//  testPage(const TestTrickView());
//  testPage(const MyBidView(8, 5));
//  testGameResultView();
}

void testGameResultView() {
  testPage(GameResultView([
    PlayerScore(player: Player(name: "Player A"), points: 130),
    PlayerScore(player: Player(name: "Player B"), points: 110),
    PlayerScore(player: Player(name: "Player C"), points: 110),
    PlayerScore(player: Player(name: "Player D"), points: 50),
  ]));
}

void testPage(Widget child) {
  runApp(MaterialApp(
    title: 'Zauberer',
    theme: ThemeData(
      primarySwatch: Colors.blue,
    ),
    localizationsDelegates: AppLocalizations.localizationsDelegates,
    supportedLocales: AppLocalizations.supportedLocales,
    home: Scaffold(
      appBar: AppBar(title: const Text("Place your cards")),
      backgroundColor: const Color(0xff158215),
      body: ExpandDisplay(
        children: [
          testPlayerStatus(),
          Expanded(child: Center(child: child)),
          testMyCards(),
        ]
      ))));
}

class TestTrickView extends StatelessWidget {
  const TestTrickView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return TrickView(
      ObservableList<TrickCard>()
        ..add(TrickCard(msg.Card(value: Value.c11, suit: Suit.heart), Player(name: "Player A")))
        ..add(TrickCard(msg.Card(value: Value.c13, suit: Suit.heart), Player(name: "Player B")))
        ..add(TrickCard(msg.Card(value: Value.c5, suit: Suit.spade), Player(name: "Player C")))
        ..add(TrickCard(msg.Card(value: Value.z, suit: null), Player(name: "You"))),
      child:
      trickText(context, text: AppLocalizations.of(context)!.youMakeTheTrick, onPressed: (){}),
    );
  }
}

Padding testMyCards() {
  return Padding(
    padding: const EdgeInsets.all(16),
    child: CardListView(
      ObservableList<msg.Card>()
        ..add(msg.Card(value: Value.z, suit: null))
        ..add(msg.Card(value: Value.c13, suit: Suit.club))
        ..add(msg.Card(value: Value.c11, suit: Suit.spade))
        ..add(msg.Card(value: Value.c12, suit: Suit.heart))
        ..add(msg.Card(value: Value.c9, suit: Suit.heart))
        ..add(msg.Card(value: Value.c10, suit: Suit.diamond))
        ..add(msg.Card(value: Value.n, suit: null))
      ));
}

Padding testPlayerStatus() {
  return Padding(
    padding: const EdgeInsets.all(16),
    child: PlayerStateView(
      ActivityState("1")
      ..startRound([
        Player(id: "1", name: "Player A"),
        Player(id: "2", name: "Player B"),
        Player(id: "3", name: "Player C"),
        Player(id: "4", name: "You"),
      ], "1")
      ..setActivePlayers(const {"4"})
      ..finishRound({
        "1": RoundInfo(total: 50),
        "2": RoundInfo(total: 90),
        "3": RoundInfo(total: 70),
        "4": RoundInfo(total: 120),
      })
      ..bid("1", 1)
      ..bid("2", 2)
      ..bid("3", 0)
      ..bid("4", 3)
      ..setTricks("1", 2)
      ..setTricks("2", 1)
      ..setTricks("3", 0)
      ..setTricks("4", 2)
    )
  );
}

extension CardEquality on msg.Card {
  bool eq(msg.Card other) {
    return value == other.value && suit == other.suit;
  }

  bool beatenBy(msg.Suit? trumpColor, msg.Card other) {
    // First wizard wins.
    if (value == Value.z) {
      return false;
    }
    if (other.value == Value.z) {
      return true;
    }

    // Jester is beaten by all cards except another jester.
    if (other.value == Value.n) {
      return false;
    }
    if (value == Value.n) {
      return true;
    }

    // Trump color wins.
    if (suit == trumpColor && other.suit != trumpColor) {
      return false;
    }
    if (other.suit == trumpColor && suit != trumpColor) {
      return true;
    }

    // Card only beats card of same suit.
    if (other.suit != suit) {
      return false;
    }

    // Card with highest index wins.
    return value.index < other.value.index;
  }
}

bool eitherOr(bool a, bool b) {
  return a && !b || b && !a;
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
  /// The player has started the application. Still initializing and connecting to the game server.
  startup,

  /// A connection with the server cannot be established, because the protocol version does not match.
  updateRequired,

  /// The application has been initialized, but no account information was found. A view is offered to create an account.
  accountCreation,

  /// The login message has been sent, waiting for acknowledge.
  connecting,

  /// The [Welcome] message has been receive.
  loggedIn,

  /// The player has not yet joined a game and is listening for other players
  /// opening games. A button is offered to create a game.
  listingGames,

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
  final Future<SharedPreferences> _prefs = SharedPreferences.getInstance();

  static const playerIdKey = "playerId";
  static const secretKey = "secret";

  GameList gameList = GameList();
  WizardModel? wizardModel;

  final ValueNotifier<ConnectionState> state = ValueNotifier(ConnectionState.startup);

  /// The message sent by the server if the connection cannot be established.
  String? errorMessage;

  static const String _serverAddress = "wss://play.haumacher.de/zauberer/ws";
  // static const String _serverAddress = "ws://homepi:8081/zauberer-test/ws";

  /// The ID of the player in this app.
  String? playerId;

  void Function(String msg)? _errorHandler;

  /// ID of the currently joined game.
  String? get gameId => currentGame?.game.gameId;

  ObservableGame? currentGame;

  /// The [WebSocketChannel] communicating with the game server.
  WebSocketChannel? _socket;

  ConnectionHandler() {
    openConnection();
  }

  void openConnection() {
    _socket?.sink.close();
    _socket = null;

    if (kDebugMode) {
      print("Opening connection.");
    }

    var socket = WebSocketChannel.connect(Uri.parse(_serverAddress));
    socket.stream.listen(_onMessage, onDone: _onClose, onError: _onError, cancelOnError: true);
    // socket.onOpen.listen(_onOpen);
    _socket = socket;

    if (kDebugMode) {
      print("Connection established.");
    }

    sendCommand(Hello(version: protocolVersion, language: PlatformDispatcher.instance.locale.languageCode));

    setState(ConnectionState.accountCreation);
    notifyListeners();
  }

  void reconnect() {
    openConnection();
  }

  void close() {
    _socket?.sink.close();
    _socket = null;
  }

  void sendCommand(Cmd login) {
    var data = login.toString();
    if (kDebugMode) {
      print("< " + data);
    }
    _socket!.sink.add(data);
  }

  void _onError(Object msg) {
    if (kDebugMode) {
      print("Socket error: " + msg.toString());
    }

    _onConnectionError();
  }

  void _onClose() {
    if (kDebugMode) {
      print("Connection closed.");
    }
    
    _onConnectionError();
  }

  void _onConnectionError() {
    close();
    setState(ConnectionState.disconnected);
    notifyListeners();
  }

  void setState(ConnectionState value) {
    state.value = value;
  }

  void _onMessage(data) {
    if (kDebugMode) {
      print("> " + data);
    }
    var msg = Msg.fromString(data);
    msg?.visitMsg(this, null);
  }

  @override
  void visitHelloResult(HelloResult self, void arg) {
    if (self.ok) {
      var lastPlayerId = playerId;
      var lastGameId = gameId;
      if (lastPlayerId != null && lastGameId != null) {
        sendCommand(Reconnect(playerId: lastPlayerId, gameId: lastGameId));
        setState(ConnectionState.connecting);
      } else {
        _prefs.then((prefs) {
          var _playerId = prefs.getString(playerIdKey);
          // TODO: Use secure storage.
          var secret = prefs.getString(secretKey);
          if (_playerId != null && secret != null) {
            playerId = _playerId;

            // Perform auto-login.
            sendCommand(Login(uid: _playerId, secret: secret));
            setState(ConnectionState.connecting);
          } else {
            setState(ConnectionState.accountCreation);
          }
        });
      }
    } else {
      setState(ConnectionState.updateRequired);
      errorMessage = self.msg;
    }
  }

  @override
  void visitAddEmailSuccess(AddEmailSuccess self, void arg) {
    // TODO: implement visitAddEmailSuccess
  }

  @override
  void visitCreateAccountResult(CreateAccountResult self, void arg) {
    var uid = self.uid;
    var secret = self.secret;

    _prefs.then((prefs) {
      prefs.setString(playerIdKey, uid);
      prefs.setString(secretKey, secret);

      setState(ConnectionState.connecting);
      sendCommand(Login(uid: uid, secret: secret));
    });
  }

  @override
  void visitVerifyEmailSuccess(VerifyEmailSuccess self, void arg) {
    // TODO: implement visitVerifyEmailSuccess
  }

  @override
  void visitWelcome(Welcome self, void arg) {
    playerId = self.playerId;
    setState(ConnectionState.loggedIn);
    sendCommand(ListGames());
  }

  @override
  void visitLoginFailed(LoginFailed self, void arg) {
    setState(ConnectionState.accountCreation);
  }

  @override
  void visitListGamesResult(ListGamesResult self, void arg) {
    gameList.setGames(self.games);
    setState(ConnectionState.listingGames);
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
      setState(ConnectionState.playing);
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
    setState(ConnectionState.waitingForStart);
    currentGame = game;
    
    notifyListeners();
  }

  @override
  void visitLeaveAnnounce(LeaveAnnounce self, void arg) {
    gameList.leave(self.gameId, self.playerId);
    if (playerId == self.playerId) {
      onLeaveGame();
    }
  }

  void leaveGame() {
    var currentGameId = gameId;
    if (currentGameId != null) {
      sendCommand(LeaveGame(gameId: currentGameId));
    }

    // Note: The leave request is not echoed by the server.
    onLeaveGame();
  }

  void onLeaveGame() {
    setState(ConnectionState.listingGames);
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
    _errorHandler!(self.message);
  }

  void onError(void Function(String msg) errorHandler) {
    _errorHandler = errorHandler;
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

  /// One of the players is about to select the trump color, because a wizard card was chosen as trump card.
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
  gameConfirmation
}

/// Client-side game state of a wizard game.
class WizardModel implements GameMsgVisitor<void, void>, GameCmdVisitor<void, String> {
  /// The ID of the player playing in this app.
  String playerId;

  /// The game we are currently playing.
  Game game;

  final ValueNotifier<WizardPhase> phase = ValueNotifier(WizardPhase.idle);

  final ValueNotifier<msg.Card?> trumpCard = ValueNotifier(null);
  
  final ObservableList<msg.Card> myCards = ObservableList();
  
  StartRound? roundInfo;

  Suit? trump;

  int expectedBids = 0;

  /// The player that won the [currentTrick].
  Player? turnWinner;

  List<PlayerScore> result = [];

  final ActivityState activityState;

  ObservableList<TrickCard> currentTrick = ObservableList();

  /// The index of the card in [currentTrick] that will win the trick.
  int highestCardIndex = -1;

  FinishRound? roundResult;

  bool startLead = false;

  WizardModel(String _playerId, this.game) :
    playerId = _playerId,
    activityState = ActivityState(_playerId);

  int get pointsEarned => roundResult!.info[playerId]!.points;

  @override
  void visitAnnounce(Announce self, void arg) {
    self.cmd!.visitGameCmd(this, self.playerId);
  }

  @override
  void visitStartRound(StartRound self, void arg) {
    roundInfo = self;
    activityState.startRound(self.players, self.startPlayer);
    trumpCard.value = self.trumpCard;
    myCards.set(self.cards);
    
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
    trumpCard.value = msg.Card(value: Value.z, suit: self.trumpSuit);
  }

  @override
  void visitStartBids(StartBids self, void arg) {
    expectedBids = 0;
    setState(WizardPhase.bidding);
  }

  @override
  void visitRequestBid(RequestBid self, void arg) {
    expectedBids = self.expected;
    setActivePlayer(self.playerId);
  }

  @override
  void visitBid(Bid self, String playerId) {
    activityState.bid(playerId, self.cnt);
  }

  @override
  void visitStartLead(StartLead self, void arg) {
    startLead = self.currentTrick.isEmpty;

    // In case of reconnect, re-establish the current trick.
    for (var trickCard in self.currentTrick) {
      currentTrick.add(TrickCard(trickCard.card!, player(trickCard.playerId)));
    }
  }

  @override
  void visitRequestLead(RequestLead self, void arg) {
    if (startLead) {
      currentTrick.clear();
      startLead = false;
    }

    setActivePlayer(self.playerId);
    setState(WizardPhase.leading);
  }

  @override
  void visitLead(Lead self, String playerId) {
    var playedCard = self.card!;

    if (playerId == this.playerId) {
      myCards.removeFirst((card) => card.eq(playedCard));
    }
    if (currentTrick.elements.isEmpty) {
      highestCardIndex = 0;
    } else {
      if (currentTrick.elements[highestCardIndex].card.beatenBy(trump, playedCard)) {
        highestCardIndex = currentTrick.elements.length;
      }
    }
    currentTrick.add(TrickCard(playedCard, activityState.getPlayer(playerId)));
  }

  @override
  void visitFinishTurn(FinishTurn self, void arg) {
    // Workaround for not announcing a new trick in the protocol.
    startLead = true;

    activityState.incTrick(self.winner!.id);
    turnWinner = self.winner;
    initConfirmations();
    activityState.setStartPlayer(turnWinner!.id);
    setState(WizardPhase.trickConfirmation);
  }

  @override
  void visitConfirmTrick(ConfirmTrick self, String playerId) {
    activityState.removeActivePlayer(playerId);
  }

  @override
  void visitFinishRound(FinishRound self, void arg) {
    roundResult = self;

    activityState.finishRound(self.info);
    initConfirmations();
    setState(WizardPhase.roundConfirmation);
  }

  @override
  void visitConfirmRound(ConfirmRound self, String playerId) {
    activityState.removeActivePlayer(playerId);
  }

  @override
  void visitFinishGame(FinishGame self, void arg) {
    result = self.scores;
    setState(WizardPhase.gameConfirmation);
  }

  @override
  void visitConfirmGame(ConfirmGame self, String arg) {
    activityState.removeActivePlayer(playerId);
  }

  void setActivePlayer(String activePlayerId) {
    activityState.setActivePlayer(activePlayerId);
  }

  void setState(WizardPhase newState) {
    phase.value = newState;
  }

  void initConfirmations() {
    activityState.setActivePlayers(roundInfo!.players.map((e) => e.id).toSet());
  }

  Player player(String playerId) {
    return roundInfo!.players.where((element) => element.id == playerId).first;
  }
}

/// The player statistics including the total amount of players, their bids, tricks, points and whether an action is required by a certain player.
class ActivityState extends ChangeNotifier {
  /// The ID of the player running the app.
  final String playerId;

  /// All players participating in the order they should be displayed.
  List<Player> _players = [];
  
  /// IDs of players that are required to act.
  Set<String> _activePlayerIds = {};
  
  /// The bid, trick and points info for players.
  final Map<String, PlayerInfo> _playerInfoById = {};

  /// The ID of the current start player (that places the first bid, or plays the first card).
  String? _startPlayer;

  ActivityState(this.playerId);

  List<Player> get players => _players;

  /// Whether this app's player is expected to act.
  ValueNotifier<bool> imActive = ValueNotifier(false);

  bool isActive(String playerId) => _activePlayerIds.contains(playerId);

  bool isStartPlayer(Player player) => player.id == _startPlayer;

  /// The player who is expected to act.
  /// Must only be called in situations where a single player is active.
  Player? get activePlayer => _activePlayerIds.isEmpty ? null : getPlayer(_activePlayerIds.first);

  PlayerInfo getInfo(String playerId) {
    return _playerInfoById[playerId]!;
  }

  void startRound(List<Player> players, String startPlayer) {
    _startPlayer = startPlayer;
    _players = players;
    players.forEach(_initInfo);
    _playerInfoById.values.forEach(_clearTricks);
    notifyListeners();
  }

  void setStartPlayer(String? startPlayer) {
    _startPlayer = startPlayer;
    notifyListeners();
  }

  void _initInfo(Player player) {
    _playerInfoById.putIfAbsent(player.id, () => PlayerInfo());
  }
  
  void _clearTricks(PlayerInfo info) {
    // Used as marker for "no bids placed yet".
    info.bid = -1;

    info.tricks = 0;
  }

  void setActivePlayer(String activePlayerId) {
    _activePlayerIds = {activePlayerId};
    updateActivity();
  }

  void setActivePlayers(Set<String> playerIds) {
    _activePlayerIds = playerIds;
    updateActivity();
  }

  void removeActivePlayer(String playerId) {
    _activePlayerIds.remove(playerId);
    updateActivity();
  }

  void updateActivity() {
    imActive.value = isActive(playerId);
    notifyListeners();
  }

  void bid(String playerId, int bid) {
    getInfo(playerId).bid = bid;
    notifyListeners();
  }

  void setTricks(String playerId, int tricks) {
    getInfo(playerId).tricks = tricks;
    notifyListeners();
  }

  void incTrick(String playerId) {
    getInfo(playerId).tricks++;
    notifyListeners();
  }

  void finishRound(Map<String, RoundInfo> pointsByPlayer) {
    pointsByPlayer.forEach(_updatePoints);
    notifyListeners();
  }

  void _updatePoints(String playerId, RoundInfo info) {
    getInfo(playerId).points = info.total;
  }

  Player getPlayer(String playerId) =>
      _players.firstWhere((player) => player.id == playerId);

}

class ObservableList<T> extends ChangeNotifier {
  List<T> _elements = [];

  List<T> get elements => _elements;

  void clear() {
    _elements.clear();
    notifyListeners();
  }

  void add(T element) {
    _elements.add(element);
    notifyListeners();
  }

  void set(List<T> elements) {
    _elements = elements;
    notifyListeners();
  }

  void removeFirst(bool Function(T) test) {
    var index = _elements.indexWhere(test);
    if (index >= 0) {
      _elements.removeAt(index);
      notifyListeners();
    }
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
      localizationsDelegates: AppLocalizations.localizationsDelegates,
      supportedLocales: AppLocalizations.supportedLocales,
      home: const HomePage()
    );
  }
}

/// Homepage view of the app.
class HomePage extends StatelessWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    connection.onError((msg) {
      showDialog(context: context, builder: (context) {
        return AlertDialog(
          title: Text(msg),
          actions: [
            ElevatedButton(
              child: Text(AppLocalizations.of(context)!.ok),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      });
    });

    return ValueListenableBuilder<ConnectionState>(
      valueListenable: connection.state,
      builder: (context, state, child) {
        if (kDebugMode) {
          print("Building HomePage: " + state.name);
        }

        switch (state) {
          case ConnectionState.startup:
          case ConnectionState.connecting:
            return Scaffold(
              appBar: AppBar(
                title: Text(AppLocalizations.of(context)!.zaubererOnline),
              ),
              body: CenteredText(AppLocalizations.of(context)!.connecting, color: Colors.black));
          case ConnectionState.accountCreation:
            return const CreateAccountView();
          case ConnectionState.disconnected:
            return Scaffold(
              appBar: AppBar(
                title: Text(AppLocalizations.of(context)!.noConnection),
              ),
              body:  Center(
                child: ElevatedButton(
                  child: Text(AppLocalizations.of(context)!.connect),
                  onPressed: () {
                    connection.reconnect();
                  },
                ),
              )
            );
          case ConnectionState.loggedIn:
            return Scaffold(
              appBar: AppBar(
                title: Text(AppLocalizations.of(context)!.zaubererOnline),
              ),
              body: CenteredText(AppLocalizations.of(context)!.loggingIn, color: Colors.black));
          case ConnectionState.listingGames:
            return ChangeObserver<GameList>(
              state: connection.gameList,
              builder: (context, gameList) {
                var openGames = gameList.openGames;
                return Scaffold(
                  appBar: AppBar(title: Text(AppLocalizations.of(context)!.joinGame)),
                  body: openGames.isEmpty ?
                    CenteredText(AppLocalizations.of(context)!.noOpenGames, color: Colors.black) :
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
            return showWaitingForStart(context);
          case ConnectionState.updateRequired:
            return Scaffold(
                appBar: AppBar(
                  title: Text(AppLocalizations.of(context)!.zaubererOnline),
                ),
                backgroundColor: Colors.deepOrange,
                body: CenteredText(connection.errorMessage ?? state.name, color: Colors.black),
            );
          default:
            return Scaffold(
              appBar: AppBar(
                title: Text(AppLocalizations.of(context)!.zaubererOnline),
              ),
              body: CenteredText("ERROR: " + state.name, color: Colors.black));
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

extension PlayerName on Player {
  String displayName(BuildContext context) => amI(id) ? AppLocalizations.of(context)!.you : name;
}

/// Whether the player of this app is the player with the given ID.
bool amI(String id) => id == connection.playerId;

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
                Text(observable.players.map((e) => e.displayName(context)).join(", "))),
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
    connection.leaveGame();
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
          case ConnectionState.listingGames:
            return showWaitingForStart(context);
          case ConnectionState.disconnected:
            return Scaffold(
              appBar: AppBar(
                title: Text(AppLocalizations.of(context)!.connectionLost),
              ),
              body:  Center(
                child: ElevatedButton(
                  child: Text(AppLocalizations.of(context)!.reconnect),
                  onPressed: () {
                    connection.reconnect();
                  },
                ),
              )
            );
          case ConnectionState.accountCreation:
            return Scaffold(
                appBar: AppBar(
                  title: Text(AppLocalizations.of(context)!.loginFailed),
                ),
                body:  Center(
                  child: ElevatedButton(
                    child: Text(AppLocalizations.of(context)!.reLogin),
                    onPressed: () {
                      Navigator.pop(context);
                    },
                  ),
                )
            );
          case ConnectionState.connecting:
            return Scaffold(
              appBar: AppBar(
                title: Text(AppLocalizations.of(context)!.zaubererOnline),
              ),
              body: CenteredText(AppLocalizations.of(context)!.reconnecting));
          case ConnectionState.waitingForStart:
            ObservableGame? game = connection.currentGame;
            if (game == null) return showWaitingForStart(context);
            return ChangeObserver<ObservableGame>(state: game,
                builder: (context, game) {
                  if (kDebugMode) {
                    print("Building GameLobbyState");
                  }
                  var players = game.players;

                  return Scaffold(
                    appBar: AppBar(
                      title: Text(AppLocalizations.of(context)!.waitingForPlayers),
                    ),
                    body: players.isEmpty ?
                    CenteredText(AppLocalizations.of(context)!.noPlayers) :
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
                                      Text((n + 1).toString() + ". " + players[n].displayName(context)))
                                ])))
                      ]
                    ),
                    floatingActionButton:
                    FloatingActionButton.extended(
                      label: Text(AppLocalizations.of(context)!.start),
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

Scaffold showWaitingForStart(BuildContext context) {
  return Scaffold(
      appBar: AppBar(
        title: Text(AppLocalizations.of(context)!.waitingForPlayers),
      ),
      body: CenteredText(AppLocalizations.of(context)!.joiningGame));
}

class WizardWidget extends StatelessWidget {
  const WizardWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        var i18n = AppLocalizations.of(context)!;
        return (await showDialog(context: context, builder: (context) => AlertDialog(
          title: Text(i18n.leaveGameTitle),
          content: Text(i18n.leaveGameQuestion),
          actions: <Widget>[
            TextButton(
              onPressed: () => Navigator.of(context).pop(/* result of the showDialog() function */ false),
              child: Text(i18n.no),
            ),
            TextButton(
              onPressed: () => Navigator.of(context).pop(true),
              child: Text(i18n.yes),
            ),
          ],
        ),
        ) ?? /* if dialog was dismissed by clicking on the background */ false);
      },
      child: buildContent(context),
    );
  }

  Widget buildContent(BuildContext context) {
    var wizardModel = connection.wizardModel!;

    return ValueListenableBuilder<WizardPhase>(
      valueListenable: wizardModel.phase,
      builder: (context, phase, child) {
        if (kDebugMode) {
          print("Building WizardWidget: " + phase.name);
        }

        var roundInfo = wizardModel.roundInfo;
        return Scaffold(
            appBar: AppBar(
              title: roundInfo == null ?
              Text(AppLocalizations.of(context)!.waitingForStart) :
              Text(AppLocalizations.of(context)!.round(
                roundInfo.round.toString(),
                roundInfo.maxRound.toString())),
            ),
            backgroundColor: const Color(0xff158215),
            body: ExpandDisplay(
                children: [
                  Row(children: [
                    Expanded(child:
                      Padding(padding: const EdgeInsets.all(16),
                        child: PlayerStateView(wizardModel.activityState)
                      )),
                    ValueListenableBuilder<msg.Card?>(
                      valueListenable: wizardModel.trumpCard,
                      builder: (context, trumpCard, child) {
                        return trumpCard == null ?
                          const SizedBox.shrink() :
                          Padding(padding: const EdgeInsets.fromLTRB(0, 16, 16, 16),
                            child: CardView(trumpCard, size: 30));
                        })
                  ]),
                  Expanded(child: Center(
                      child: createBody(context, phase)
                  )),
                  Padding(padding: const EdgeInsets.all(16),
                      child: CardListView(wizardModel.myCards,
                        onTap: (card) {
                          connection.sendCommand(msg.Lead(card: card));
                        },
                      )),
                ]));
      });
// createBody(context, phase));
  }

  Widget createBody(BuildContext context, WizardPhase phase) {
    if (kDebugMode) {
      print("Building WizardWidget body: " + phase.name);
    }

    var i18n = AppLocalizations.of(context)!;
    var wizardModel = connection.wizardModel!;
    switch (phase) {
      case WizardPhase.idle:
      case WizardPhase.created:
        return CenteredText(i18n.waitingForCards);
      case WizardPhase.cardsGiven:
        return CenteredText(i18n.waitingForBidRequest);
      case WizardPhase.trumpSelection:
        return ValueListenableBuilder<bool>(
          valueListenable: wizardModel.activityState.imActive,
          builder: (context, imActive, child) {
            return imActive ?
              const TrumpSelectionView() :
              WaitingForView(i18n.selectsTrump, key: const Key("selectsTrump"));
          });
      case WizardPhase.bidding:
        return ValueListenableBuilder<bool>(
          valueListenable: wizardModel.activityState.imActive,
          builder: (context, imActive, child) {
            return imActive ?
              BidView(wizardModel.roundInfo!.round, wizardModel.expectedBids) :
              WaitingForView(i18n.waitingForBid, key: const Key("waitingForBid"));
          });
      case WizardPhase.leading:
        return const LeadingView();
      case WizardPhase.trickConfirmation:
        return Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TrickView(wizardModel.currentTrick,
                child: ValueListenableBuilder<bool>(
                  valueListenable: wizardModel.activityState.imActive,
                  builder: (context, amActive, child) {
                    return amActive ?
                      trickText(context,
                        text: amI(wizardModel.turnWinner!.id) ?
                          i18n.youMakeTheTrick :
                          i18n.playerMakesTheTrick(
                            wizardModel.turnWinner!.displayName(context)),
                        onPressed: () {
                          connection.sendCommand(msg.ConfirmTrick());
                        }) :
                      trickText(context, text: i18n.waitingForOtherPlayers);
                  },
                ) 
              ),
            ]));
      case WizardPhase.roundConfirmation:
        return Center(
          child: ValueListenableBuilder<bool>(
            valueListenable: wizardModel.activityState.imActive,
            builder: (context, amActive, child) {
              return amActive ?
                Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Text(i18n.youGetPoints(wizardModel.pointsEarned),
                      style: const TextStyle(color: Colors.white, fontSize: 24, fontWeight: FontWeight.bold)),
                    Padding(
                      padding: const EdgeInsets.fromLTRB(0, 8, 0, 0),
                      child: ElevatedButton(
                        onPressed: () {
                          connection.sendCommand(msg.ConfirmRound());
                        },
                        child: Text(i18n.ok)))
                  ],
                ) :
                Text(i18n.waitingForOtherPlayers,
                  style: const TextStyle(color: Colors.white, fontSize: 24, fontWeight: FontWeight.bold));
            }));
      case WizardPhase.gameConfirmation:
        return GameResultView(wizardModel.result);
      default:
        return CenteredText("ERROR: " + phase.name);
    }
  }
}

class GameResultView extends StatelessWidget {
  final List<PlayerScore> result;

  const GameResultView(this.result, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    int rank = 1;
    int lastScore = result[0].points;
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child:
      Center(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Table(
              columnWidths: const {
                0: FixedColumnWidth(75),
                1: FlexColumnWidth(1),
                2: FixedColumnWidth(75),
              },

              children: [
                TableRow(
                  decoration: const BoxDecoration(
                    color: Colors.grey,
                    border: Border(
                      bottom: BorderSide(width: 2))),
                  children: [
                    cell(AppLocalizations.of(context)!.rank),
                    cell(AppLocalizations.of(context)!.player),
                    cell(AppLocalizations.of(context)!.score),
                  ]
                ),
                for (var score in result)
                  TableRow(
                    decoration: const BoxDecoration(color: Colors.white),
                    children: [
                      cell((score.points == lastScore ? rank : ++rank).toString()),
                      cell(score.player!.displayName(context)),
                      cell((lastScore = score.points).toString()),
                    ])
              ]),
            Padding(
              padding: const EdgeInsets.fromLTRB(0, 16, 0, 0),
              child: ElevatedButton(
                onPressed: () {
                  connection.sendCommand(msg.ConfirmGame());
                  Navigator.pop(context);
                },
                child: Text(AppLocalizations.of(context)!.leaveGame)))
          ]))
    );
  }

  TableCell cell(final String content) {
    return TableCell(
      verticalAlignment: TableCellVerticalAlignment.top,
      child: Padding(
          padding: const EdgeInsets.all(8),
          child: Text(content)));
  }
}

/// Message text centered on the game area.
class CenteredText extends StatelessWidget {
  final String text;
  final Color color;

  const CenteredText(this.text, {Key? key, this.color = Colors.white}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20),
        child: Center(
            child: Text(text,
              textAlign: TextAlign.justify,
              style: TextStyle( 
                color: color,
                fontSize: 20,
              ),
            )
        )
    );
  }
}

Widget trickText(BuildContext context, {String? text, void Function()? onPressed}) {
  return trickTitle(context, child: text == null ? null :
    Text(text, style: const TextStyle(color: Colors.white, fontSize: 24, fontWeight: FontWeight.bold)), onPressed: onPressed);
}

Widget trickTitle(BuildContext context, {Widget? child, void Function()? onPressed}) {
  return Column(children: [
    Visibility(
        maintainSize: true,
        maintainAnimation: true,
        maintainState: true,
        visible: child != null,
        child: child ?? const Text("", style: TextStyle(color: Colors.white, fontSize: 24, fontWeight: FontWeight.bold))),
    Padding(
        padding: const EdgeInsets.fromLTRB(0, 8, 0, 0),
        child: Visibility(
            maintainSize: true,
            maintainAnimation: true,
            maintainState: true,
            visible: onPressed != null,
            child: ElevatedButton(
              child: Text(AppLocalizations.of(context)!.ok),
              onPressed: onPressed,
            )
        ))
  ]);
}

class TrumpSelectionView extends StatelessWidget {
  const TrumpSelectionView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        CardListView(ObservableList<msg.Card>()..set([
          msg.Card(suit: Suit.diamond, value: Value.z),
          msg.Card(suit: Suit.heart, value: Value.z),
          msg.Card(suit: Suit.spade, value: Value.z),
          msg.Card(suit: Suit.club, value: Value.z),
        ]), onTap: (card) {
          connection.sendCommand(msg.SelectTrump(trumpSuit: card.suit!));
        }),
        Padding(
          padding: const EdgeInsets.fromLTRB(0, 16, 0, 0),
          child:
            trickText(context, text: AppLocalizations.of(context)!.selectTrump)
        )
      ]);
  }
}

class LeadingView extends StatelessWidget {
  const LeadingView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var i18n = AppLocalizations.of(context)!;
    var wizardModel = connection.wizardModel!;

    return TrickView(wizardModel.currentTrick,
      child: ValueListenableBuilder<bool>(
        valueListenable: wizardModel.activityState.imActive,
        builder: (context, imActive, child) {
          return imActive ?
            trickText(context, text: AppLocalizations.of(context)!.itsYourTurn) :
            trickTitle(context, child: WaitingForView(i18n.waitingForPlayersCard));
        })
    );
  }
}

class WaitingForView extends StatelessWidget {
  final String Function(String) messageForPlayer;

  const WaitingForView(this.messageForPlayer, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var wizardModel = connection.wizardModel!;
    
    return ChangeObserver<ActivityState>(
      state: wizardModel.activityState,
      builder: (context, activityState) {
        var activePlayer = activityState.activePlayer;
        var message = activePlayer == null ? "" : messageForPlayer(activePlayer.displayName(context));

        return Text(message,
            style: const TextStyle(
              color: Colors.white,
              fontSize: 24,
              fontWeight: FontWeight.bold));
      });
  }
}

/// [Widget] to place a bid.
class BidView extends StatelessWidget {
  // The number of cards given in this round (equal to the round ID).
  final int maxBids;

  /// The number of bids already placed by other players.
  final int bidsSoFar;

  const BidView(this.maxBids, this.bidsSoFar, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Text(AppLocalizations.of(context)!.placeYourBid,
            style: const TextStyle(color: Colors.white, fontSize: 24, fontWeight: FontWeight.bold)),
          Padding(
              padding: const EdgeInsets.all(15),
              child: Wrap(
                  direction: Axis.horizontal,
                  children: options()))]);
  }

  List<Widget> options() {
    List<Widget> result = [];
    var freeTricks = maxBids - bidsSoFar;
    for (var x = 0; x <= maxBids ; x++) {
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
                backgroundColor: x <= freeTricks ? Colors.lightBlue : Colors.orange
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

  final ObservableList<TrickCard> trick;
  final Widget? child;

  const TrickView(this.trick, {this.child, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          buildTrick(context),
          if (child != null)
            Padding(
              padding: const EdgeInsets.fromLTRB(0, 16, 0, 0),
              child: child,
            )
        ]));
  }

  Widget buildTrick(BuildContext context) {
    return ChangeObserver<ObservableList<TrickCard>>(state: trick, builder: (context, trick) {
      List<Widget> cards = [];
      var cnt = trick.elements.length;
      for (var n = 0; n < cnt; n++) {
        var card = trick.elements[n];
        var highest = cnt > 1 && n == connection.wizardModel!.highestCardIndex;

        cards.add(Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              CardView(card.card, highest: highest),
              Padding(padding: const EdgeInsets.fromLTRB(0, 5, 0, 0),
                child: Text(card.player.displayName(context),
                  style: highest ?
                    const TextStyle(color: Colors.white, fontWeight: FontWeight.bold) :
                    const TextStyle(color: Colors.white)),
              )
            ]));
      }

      return Wrap(
        spacing: 5,
        runSpacing: 10,
        children: cards
      );
    });
  }
}

class CardListView extends StatelessWidget {
  final ObservableList<msg.Card> cards;
  final void Function(msg.Card)? onTap;

  const CardListView(this.cards, {this.onTap, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ChangeObserver<ObservableList<msg.Card>>(state: cards, builder: (context, cards) {
      return Wrap(
          spacing: 5,
          runSpacing: 5,
          children:
          cards.elements.map((card) => CardView(card, onTap: onTap)).toList()
      );
    });
  }
}

class PlayerStateView extends StatelessWidget {
  final ActivityState state;

  const PlayerStateView(this.state, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ChangeObserver<ActivityState>(state: state, builder: (context, state) {
      return Wrap(
        direction: Axis.horizontal,
        alignment: WrapAlignment.center,
        spacing: 5,
        runSpacing: 10,
        children: state.players.map((player) => playerStateView(context, player)).toList(),
      );
    });
  }

  Widget playerStateView(BuildContext context, Player player) {
    var info = state.getInfo(player.id);
    var border = state.isStartPlayer(player) ?
      Border.all(color: Colors.deepOrangeAccent, width: 2) :
      Border.all(style: BorderStyle.none);
    return DecoratedBox(
      decoration: state.isActive(player.id) ?
        BoxDecoration(color: Colors.amber, borderRadius: BorderRadius.circular(10), border: border) :
        BoxDecoration(color: const Color(0xff6cac6c), borderRadius: BorderRadius.circular(10), border: border),
      child: Padding(
        padding: const EdgeInsets.all(8),
        child:
        Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Padding(
              padding: const EdgeInsets.fromLTRB(0, 0, 0, 5),
              child:
              Text(player.displayName(context) + " (" + info.points.toString() + ")")),
            trickView(info)
          ])
      ));
  }

  Widget trickView(PlayerInfo info) {
    if (info.bid < 0) {
      return const Text("");
    }
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
  final bool highest;
  final double size;
  final void Function(msg.Card)? onTap;

  const CardView(this.card, {this.highest = false, this.size = 50, Key? key, this.onTap}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var handler = onTap;

    if (handler == null) {
      return contents(context);
    }

    return GestureDetector(
        onTap: () => handler(card),
        child: contents(context));
  }

  Widget contents(BuildContext context) {
    return Container(
        decoration: BoxDecoration(
            border: Border.all(
              color: highest ? Colors.purple : Colors.black,
              width: 2,
            ),
            borderRadius: const BorderRadius.all(Radius.circular(8)),
            color: Colors.white,
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
                          style: TextStyle(fontSize: 25 * size / 50, fontWeight: FontWeight.bold))),
                    CustomPaint(painter: suitPainter, size: Size(size, size))
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

/// View displaying a form with information required for account creation.
class CreateAccountView extends StatefulWidget {
  const CreateAccountView({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return CreateAccountViewState();
  }
}

class CreateAccountViewState extends State<CreateAccountView> {
  final GlobalKey<FormState> _formKey = GlobalKey(debugLabel: "createAccountState");
  final GlobalKey<FormFieldState<String>> _nickName = GlobalKey();
  final GlobalKey<FormFieldState<String>> _email = GlobalKey();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(AppLocalizations.of(context)!.login),
      ),
      body: Form(
        key: _formKey,
        child: Padding(
          padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 16),
          child: Column(
            children: [
              TextFormField(
                key: _nickName,
                decoration: InputDecoration(hintText: AppLocalizations.of(context)!.nickname),
                validator: (String? value) {
                  if (value == null || value.isEmpty) {
                    return AppLocalizations.of(context)!.nicknameMustNotBeEmpty;
                  }
                  return null;
                },
              ),
              TextFormField(
                key: _email,
                decoration: InputDecoration(hintText: AppLocalizations.of(context)!.email),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 16),
                child: ElevatedButton(
                  child: Text(AppLocalizations.of(context)!.createAccount),
                  onPressed: () {
                    if (_formKey.currentState!.validate()) {
                      connection.sendCommand(
                        CreateAccount(
                          nickname: _nickName.currentState!.value!,
                        ),
                      );
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

class ChangeObserver<S extends ChangeNotifier> extends StatefulWidget {
  final S state;
  final Widget Function(BuildContext, S) builder;

  const ChangeObserver({Key? key, required this.state, required this.builder}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return _ChangeObserverState<ChangeObserver<S>, S>();
  }

}

class _ChangeObserverState<T extends ChangeObserver<S>, S extends ChangeNotifier> extends State<T> {
  late S observable;
  late Widget Function(BuildContext, S) builder;

  @override
  void initState() {
    super.initState();

    observable = widget.state;
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

