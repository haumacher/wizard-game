// ignore: unused_import
import 'package:intl/intl.dart' as intl;
import 'app_localizations.dart';

// ignore_for_file: type=lint

/// The translations for English (`en`).
class AppLocalizationsEn extends AppLocalizations {
  AppLocalizationsEn([String locale = 'en']) : super(locale);

  @override
  String get ok => 'OK';

  @override
  String get zaubererOnline => 'Zauberer online';

  @override
  String get connecting => 'Connecting...';

  @override
  String get noConnection => 'No Connection';

  @override
  String get connect => 'Connect';

  @override
  String get loggingIn => 'Logging in...';

  @override
  String get joinGame => 'Join a game';

  @override
  String get noOpenGames => 'No open games.';

  @override
  String get you => 'You';

  @override
  String get connectionLost => 'Connection lost';

  @override
  String get reconnect => 'Re-Connect';

  @override
  String get reconnecting => 'Reconnecting...';

  @override
  String get waitingForPlayers => 'Waiting for players';

  @override
  String get waitingForOtherPlayers => 'Waiting for other players...';

  @override
  String get noPlayers => 'No players in this game.';

  @override
  String get start => 'Start';

  @override
  String get joiningGame => 'Joining game...';

  @override
  String get waitingForStart => 'Waiting for start...';

  @override
  String round(Object n, Object rounds) {
    return 'Round $n of $rounds';
  }

  @override
  String get waitingForCards => 'Waiting for cards...';

  @override
  String get waitingForBidRequest => 'Waiting for bid request...';

  @override
  String selectsTrump(Object player) {
    return '$player selects the trump color...';
  }

  @override
  String waitingForBid(Object player) {
    return 'Waiting for $player\'s bid.';
  }

  @override
  String get youMakeTheTrick => 'You make the trick!';

  @override
  String playerMakesTheTrick(Object player) {
    return '$player makes the trick!';
  }

  @override
  String youGetPoints(Object points) {
    return 'You get $points points!';
  }

  @override
  String get leaveGame => 'Leave game';

  @override
  String get selectTrump => 'Select the trump color!';

  @override
  String get itsYourTurn => 'It\'s your turn!';

  @override
  String waitingForPlayersCard(Object player) {
    return 'Waiting for $player\'s card.';
  }

  @override
  String get placeYourBid => 'Place your bid!';

  @override
  String get login => 'Login to Wizard server';

  @override
  String get nickname => 'Nick name';

  @override
  String get nicknameMustNotBeEmpty => 'Nick name must not be empty.';

  @override
  String get email => 'E-mail';

  @override
  String get createAccount => 'Create account';

  @override
  String get loginFailed => 'Login failed';

  @override
  String get reLogin => 'Retry login';

  @override
  String get rank => 'Rank';

  @override
  String get player => 'Player';

  @override
  String get score => 'Score';

  @override
  String get leaveGameTitle => 'Leave game?';

  @override
  String get leaveGameQuestion =>
      'If you now leave the game, you play mates cannot continue playing. Would you like to leave the game anyways?';

  @override
  String get no => 'No';

  @override
  String get yes => 'Yes';
}
