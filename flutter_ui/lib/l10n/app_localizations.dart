import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:intl/intl.dart' as intl;

import 'app_localizations_de.dart';
import 'app_localizations_en.dart';

// ignore_for_file: type=lint

/// Callers can lookup localized strings with an instance of AppLocalizations
/// returned by `AppLocalizations.of(context)`.
///
/// Applications need to include `AppLocalizations.delegate()` in their app's
/// `localizationDelegates` list, and the locales they support in the app's
/// `supportedLocales` list. For example:
///
/// ```dart
/// import 'l10n/app_localizations.dart';
///
/// return MaterialApp(
///   localizationsDelegates: AppLocalizations.localizationsDelegates,
///   supportedLocales: AppLocalizations.supportedLocales,
///   home: MyApplicationHome(),
/// );
/// ```
///
/// ## Update pubspec.yaml
///
/// Please make sure to update your pubspec.yaml to include the following
/// packages:
///
/// ```yaml
/// dependencies:
///   # Internationalization support.
///   flutter_localizations:
///     sdk: flutter
///   intl: any # Use the pinned version from flutter_localizations
///
///   # Rest of dependencies
/// ```
///
/// ## iOS Applications
///
/// iOS applications define key application metadata, including supported
/// locales, in an Info.plist file that is built into the application bundle.
/// To configure the locales supported by your app, you’ll need to edit this
/// file.
///
/// First, open your project’s ios/Runner.xcworkspace Xcode workspace file.
/// Then, in the Project Navigator, open the Info.plist file under the Runner
/// project’s Runner folder.
///
/// Next, select the Information Property List item, select Add Item from the
/// Editor menu, then select Localizations from the pop-up menu.
///
/// Select and expand the newly-created Localizations item then, for each
/// locale your application supports, add a new item and select the locale
/// you wish to add from the pop-up menu in the Value field. This list should
/// be consistent with the languages listed in the AppLocalizations.supportedLocales
/// property.
abstract class AppLocalizations {
  AppLocalizations(String locale)
      : localeName = intl.Intl.canonicalizedLocale(locale.toString());

  final String localeName;

  static AppLocalizations? of(BuildContext context) {
    return Localizations.of<AppLocalizations>(context, AppLocalizations);
  }

  static const LocalizationsDelegate<AppLocalizations> delegate =
      _AppLocalizationsDelegate();

  /// A list of this localizations delegate along with the default localizations
  /// delegates.
  ///
  /// Returns a list of localizations delegates containing this delegate along with
  /// GlobalMaterialLocalizations.delegate, GlobalCupertinoLocalizations.delegate,
  /// and GlobalWidgetsLocalizations.delegate.
  ///
  /// Additional delegates can be added by appending to this list in
  /// MaterialApp. This list does not have to be used at all if a custom list
  /// of delegates is preferred or required.
  static const List<LocalizationsDelegate<dynamic>> localizationsDelegates =
      <LocalizationsDelegate<dynamic>>[
    delegate,
    GlobalMaterialLocalizations.delegate,
    GlobalCupertinoLocalizations.delegate,
    GlobalWidgetsLocalizations.delegate,
  ];

  /// A list of this localizations delegate's supported locales.
  static const List<Locale> supportedLocales = <Locale>[
    Locale('de'),
    Locale('en')
  ];

  /// No description provided for @ok.
  ///
  /// In en, this message translates to:
  /// **'OK'**
  String get ok;

  /// No description provided for @zaubererOnline.
  ///
  /// In en, this message translates to:
  /// **'Zauberer online'**
  String get zaubererOnline;

  /// No description provided for @connecting.
  ///
  /// In en, this message translates to:
  /// **'Connecting...'**
  String get connecting;

  /// No description provided for @noConnection.
  ///
  /// In en, this message translates to:
  /// **'No Connection'**
  String get noConnection;

  /// No description provided for @connect.
  ///
  /// In en, this message translates to:
  /// **'Connect'**
  String get connect;

  /// No description provided for @loggingIn.
  ///
  /// In en, this message translates to:
  /// **'Logging in...'**
  String get loggingIn;

  /// No description provided for @joinGame.
  ///
  /// In en, this message translates to:
  /// **'Join a game'**
  String get joinGame;

  /// No description provided for @noOpenGames.
  ///
  /// In en, this message translates to:
  /// **'No open games.'**
  String get noOpenGames;

  /// No description provided for @you.
  ///
  /// In en, this message translates to:
  /// **'You'**
  String get you;

  /// No description provided for @connectionLost.
  ///
  /// In en, this message translates to:
  /// **'Connection lost'**
  String get connectionLost;

  /// No description provided for @reconnect.
  ///
  /// In en, this message translates to:
  /// **'Re-Connect'**
  String get reconnect;

  /// No description provided for @reconnecting.
  ///
  /// In en, this message translates to:
  /// **'Reconnecting...'**
  String get reconnecting;

  /// No description provided for @waitingForPlayers.
  ///
  /// In en, this message translates to:
  /// **'Waiting for players'**
  String get waitingForPlayers;

  /// No description provided for @waitingForOtherPlayers.
  ///
  /// In en, this message translates to:
  /// **'Waiting for other players...'**
  String get waitingForOtherPlayers;

  /// No description provided for @noPlayers.
  ///
  /// In en, this message translates to:
  /// **'No players in this game.'**
  String get noPlayers;

  /// No description provided for @start.
  ///
  /// In en, this message translates to:
  /// **'Start'**
  String get start;

  /// No description provided for @joiningGame.
  ///
  /// In en, this message translates to:
  /// **'Joining game...'**
  String get joiningGame;

  /// No description provided for @waitingForStart.
  ///
  /// In en, this message translates to:
  /// **'Waiting for start...'**
  String get waitingForStart;

  /// No description provided for @round.
  ///
  /// In en, this message translates to:
  /// **'Round {n} of {rounds}'**
  String round(Object n, Object rounds);

  /// No description provided for @waitingForCards.
  ///
  /// In en, this message translates to:
  /// **'Waiting for cards...'**
  String get waitingForCards;

  /// No description provided for @waitingForBidRequest.
  ///
  /// In en, this message translates to:
  /// **'Waiting for bid request...'**
  String get waitingForBidRequest;

  /// No description provided for @selectsTrump.
  ///
  /// In en, this message translates to:
  /// **'{player} selects the trump color...'**
  String selectsTrump(Object player);

  /// No description provided for @waitingForBid.
  ///
  /// In en, this message translates to:
  /// **'Waiting for {player}\'s bid.'**
  String waitingForBid(Object player);

  /// No description provided for @youMakeTheTrick.
  ///
  /// In en, this message translates to:
  /// **'You make the trick!'**
  String get youMakeTheTrick;

  /// No description provided for @playerMakesTheTrick.
  ///
  /// In en, this message translates to:
  /// **'{player} makes the trick!'**
  String playerMakesTheTrick(Object player);

  /// No description provided for @youGetPoints.
  ///
  /// In en, this message translates to:
  /// **'You get {points} points!'**
  String youGetPoints(Object points);

  /// No description provided for @leaveGame.
  ///
  /// In en, this message translates to:
  /// **'Leave game'**
  String get leaveGame;

  /// No description provided for @selectTrump.
  ///
  /// In en, this message translates to:
  /// **'Select the trump color!'**
  String get selectTrump;

  /// No description provided for @itsYourTurn.
  ///
  /// In en, this message translates to:
  /// **'It\'s your turn!'**
  String get itsYourTurn;

  /// No description provided for @waitingForPlayersCard.
  ///
  /// In en, this message translates to:
  /// **'Waiting for {player}\'s card.'**
  String waitingForPlayersCard(Object player);

  /// No description provided for @placeYourBid.
  ///
  /// In en, this message translates to:
  /// **'Place your bid!'**
  String get placeYourBid;

  /// No description provided for @login.
  ///
  /// In en, this message translates to:
  /// **'Login to Wizard server'**
  String get login;

  /// No description provided for @nickname.
  ///
  /// In en, this message translates to:
  /// **'Nick name'**
  String get nickname;

  /// No description provided for @nicknameMustNotBeEmpty.
  ///
  /// In en, this message translates to:
  /// **'Nick name must not be empty.'**
  String get nicknameMustNotBeEmpty;

  /// No description provided for @email.
  ///
  /// In en, this message translates to:
  /// **'E-mail'**
  String get email;

  /// No description provided for @createAccount.
  ///
  /// In en, this message translates to:
  /// **'Create account'**
  String get createAccount;

  /// No description provided for @loginFailed.
  ///
  /// In en, this message translates to:
  /// **'Login failed'**
  String get loginFailed;

  /// No description provided for @reLogin.
  ///
  /// In en, this message translates to:
  /// **'Retry login'**
  String get reLogin;

  /// No description provided for @rank.
  ///
  /// In en, this message translates to:
  /// **'Rank'**
  String get rank;

  /// No description provided for @player.
  ///
  /// In en, this message translates to:
  /// **'Player'**
  String get player;

  /// No description provided for @score.
  ///
  /// In en, this message translates to:
  /// **'Score'**
  String get score;

  /// No description provided for @leaveGameTitle.
  ///
  /// In en, this message translates to:
  /// **'Leave game?'**
  String get leaveGameTitle;

  /// No description provided for @leaveGameQuestion.
  ///
  /// In en, this message translates to:
  /// **'If you now leave the game, you play mates cannot continue playing. Would you like to leave the game anyways?'**
  String get leaveGameQuestion;

  /// No description provided for @no.
  ///
  /// In en, this message translates to:
  /// **'No'**
  String get no;

  /// No description provided for @yes.
  ///
  /// In en, this message translates to:
  /// **'Yes'**
  String get yes;
}

class _AppLocalizationsDelegate
    extends LocalizationsDelegate<AppLocalizations> {
  const _AppLocalizationsDelegate();

  @override
  Future<AppLocalizations> load(Locale locale) {
    return SynchronousFuture<AppLocalizations>(lookupAppLocalizations(locale));
  }

  @override
  bool isSupported(Locale locale) =>
      <String>['de', 'en'].contains(locale.languageCode);

  @override
  bool shouldReload(_AppLocalizationsDelegate old) => false;
}

AppLocalizations lookupAppLocalizations(Locale locale) {
  // Lookup logic when only language code is specified.
  switch (locale.languageCode) {
    case 'de':
      return AppLocalizationsDe();
    case 'en':
      return AppLocalizationsEn();
  }

  throw FlutterError(
      'AppLocalizations.delegate failed to load unsupported locale "$locale". This is likely '
      'an issue with the localizations generation tool. Please file an issue '
      'on GitHub with a reproducible sample app and the gen-l10n configuration '
      'that was used.');
}
