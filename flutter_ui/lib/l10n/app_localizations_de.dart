// ignore: unused_import
import 'package:intl/intl.dart' as intl;
import 'app_localizations.dart';

// ignore_for_file: type=lint

/// The translations for German (`de`).
class AppLocalizationsDe extends AppLocalizations {
  AppLocalizationsDe([String locale = 'de']) : super(locale);

  @override
  String get ok => 'OK';

  @override
  String get zaubererOnline => 'Zauberer online';

  @override
  String get connecting => 'Verbinde...';

  @override
  String get noConnection => 'Keine Verbindung';

  @override
  String get connect => 'Verbinden';

  @override
  String get loggingIn => 'Anmeldung...';

  @override
  String get joinGame => 'Spiel beitreten';

  @override
  String get noOpenGames => 'Keine offenen Spiele.';

  @override
  String get you => 'Du';

  @override
  String get connectionLost => 'Verbindung verloren';

  @override
  String get reconnect => 'Neu verbinden';

  @override
  String get reconnecting => 'Verbinde neu...';

  @override
  String get waitingForPlayers => 'Warte auf Mitspieler';

  @override
  String get waitingForOtherPlayers => 'Warte auf die Mitspieler...';

  @override
  String get noPlayers => 'Keine Sieler in diesem Spiel.';

  @override
  String get start => 'Start';

  @override
  String get joiningGame => 'Betrete Spiel...';

  @override
  String get waitingForStart => 'Warte auf Start...';

  @override
  String round(Object n, Object rounds) {
    return 'Runde $n von $rounds';
  }

  @override
  String get waitingForCards => 'Warte auf Karten...';

  @override
  String get waitingForBidRequest => 'Warte auf Aufforderung zur Vorhersage...';

  @override
  String selectsTrump(Object player) {
    return '$player wählt die Trumpffarbe...';
  }

  @override
  String waitingForBid(Object player) {
    return 'Warte auf ${player}s Vorhersage.';
  }

  @override
  String get youMakeTheTrick => 'Du machst den Stich!';

  @override
  String playerMakesTheTrick(Object player) {
    return '$player macht den Stich!';
  }

  @override
  String youGetPoints(Object points) {
    return 'Du erhälst $points Punkte!';
  }

  @override
  String get leaveGame => 'Spiel verlassen.';

  @override
  String get selectTrump => 'Wähle die Trumpffarbe!';

  @override
  String get itsYourTurn => 'Du bist am Zug!';

  @override
  String waitingForPlayersCard(Object player) {
    return 'Warte auf ${player}s Karte.';
  }

  @override
  String get placeYourBid => 'Mache deine Vorhersage!';

  @override
  String get login => 'Anmeldung am Wizard-Server';

  @override
  String get nickname => 'Spielername';

  @override
  String get nicknameMustNotBeEmpty => 'Der Spielername darf nicht leer sein.';

  @override
  String get email => 'E-Mail';

  @override
  String get createAccount => 'Account anlegen';

  @override
  String get loginFailed => 'Login fehlgeschlagen';

  @override
  String get reLogin => 'Neu anmelden';

  @override
  String get rank => 'Rang';

  @override
  String get player => 'Spieler';

  @override
  String get score => 'Punkte';

  @override
  String get leaveGameTitle => 'Spiel verlassen?';

  @override
  String get leaveGameQuestion =>
      'Wenn Du das Spiel verlässt, können Deine Mitspieler nicht weiterspielen. Willst Du das Spiel trotzdem verlassen?';

  @override
  String get no => 'Nein';

  @override
  String get yes => 'Ja';
}
