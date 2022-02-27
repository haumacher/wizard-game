import 'package:jsontool/jsontool.dart';

/// Common functionality for JSON generation and parsing.
abstract class _JsonObject {
	@override
	String toString() {
		var buffer = StringBuffer();
		writeTo(jsonStringWriter(buffer));
		return buffer.toString();
	}

	/// The ID to announce the type of the object.
	String _jsonType();

	/// Reads the object contents (after the type information).
	void _readContent(JsonReader json) {
		json.expectObject();
		while (json.hasNextKey()) {
			var key = json.nextKey();
			_readProperty(key!, json);
		}
	}

	/// Reads the value of the property with the given name.
	void _readProperty(String key, JsonReader json) {
		json.skipAnyValue();
	}

	/// Writes this object to the given writer (including type information).
	void writeTo(JsonSink json) {
		json.startArray();
		json.addString(_jsonType());
		writeContent(json);
		json.endArray();
	}

	/// Writes the contents of this object to the given writer (excluding type information).
	void writeContent(JsonSink json) {
		json.startObject();
		_writeProperties(json);
		json.endObject();
	}

	/// Writes all key/value pairs of this object.
	void _writeProperties(JsonSink json) {
		// No properties.
	}
}

///  Information about a game.
class Game extends _JsonObject {
	///  A unique identifier of the game used to reference this game in messages.
	String gameId;

	///  The players that have joined this game.
	List<Player> players;

	/// Creates a Game.
	Game({
			this.gameId = "", 
			this.players = const [], 
	});

	/// Parses a Game from a string source.
	static Game? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a Game instance from the given reader.
	static Game read(JsonReader json) {
		Game result = Game();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "Game";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "gameId": {
				gameId = json.expectString();
				break;
			}
			case "players": {
				json.expectArray();
				players = [];
				while (json.hasNext()) {
					if (!json.tryNull()) {
						players.add(Player.read(json));
					}
				}
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("gameId");
		json.addString(gameId);

		json.addKey("players");
		json.startArray();
		for (var _element in players) {
			_element.writeContent(json);
		}
		json.endArray();
	}

}

///  A player of a {@link Game}.
class Player extends _JsonObject {
	///  A technical ID of the player used to reference this player in messages.
	String id;

	///  A nick name for the player to display to other users.
	String name;

	/// Creates a Player.
	Player({
			this.id = "", 
			this.name = "", 
	});

	/// Parses a Player from a string source.
	static Player? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a Player instance from the given reader.
	static Player read(JsonReader json) {
		Player result = Player();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "Player";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "id": {
				id = json.expectString();
				break;
			}
			case "name": {
				name = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("id");
		json.addString(id);

		json.addKey("name");
		json.addString(name);
	}

}

///  The suit of a card.
enum Suit {
	diamond,
	heart,
	spade,
	club,
}

/// Writes a value of Suit to a JSON stream.
void writeSuit(JsonSink json, Suit value) {
	switch (value) {
		case Suit.diamond: json.addString("diamond"); break;
		case Suit.heart: json.addString("heart"); break;
		case Suit.spade: json.addString("spade"); break;
		case Suit.club: json.addString("club"); break;
		default: throw ("No such literal: " + value.name);
	}
}

/// Reads a value of Suit from a JSON stream.
Suit readSuit(JsonReader json) {
	switch (json.expectString()) {
		case "diamond": return Suit.diamond;
		case "heart": return Suit.heart;
		case "spade": return Suit.spade;
		case "club": return Suit.club;
		default: return Suit.diamond;
	}
}

///  Possible card values.
enum Value {
	///  A jester.
	n,
	c1,
	c2,
	c3,
	c4,
	c5,
	c6,
	c7,
	c8,
	c9,
	c10,
	c11,
	c12,
	c13,
	///  A wizard.
	z,
}

/// Writes a value of Value to a JSON stream.
void writeValue(JsonSink json, Value value) {
	switch (value) {
		case Value.n: json.addString("N"); break;
		case Value.c1: json.addString("C1"); break;
		case Value.c2: json.addString("C2"); break;
		case Value.c3: json.addString("C3"); break;
		case Value.c4: json.addString("C4"); break;
		case Value.c5: json.addString("C5"); break;
		case Value.c6: json.addString("C6"); break;
		case Value.c7: json.addString("C7"); break;
		case Value.c8: json.addString("C8"); break;
		case Value.c9: json.addString("C9"); break;
		case Value.c10: json.addString("C10"); break;
		case Value.c11: json.addString("C11"); break;
		case Value.c12: json.addString("C12"); break;
		case Value.c13: json.addString("C13"); break;
		case Value.z: json.addString("Z"); break;
		default: throw ("No such literal: " + value.name);
	}
}

/// Reads a value of Value from a JSON stream.
Value readValue(JsonReader json) {
	switch (json.expectString()) {
		case "N": return Value.n;
		case "C1": return Value.c1;
		case "C2": return Value.c2;
		case "C3": return Value.c3;
		case "C4": return Value.c4;
		case "C5": return Value.c5;
		case "C6": return Value.c6;
		case "C7": return Value.c7;
		case "C8": return Value.c8;
		case "C9": return Value.c9;
		case "C10": return Value.c10;
		case "C11": return Value.c11;
		case "C12": return Value.c12;
		case "C13": return Value.c13;
		case "Z": return Value.z;
		default: return Value.n;
	}
}

///  A card of the Wizard game.
class Card extends _JsonObject {
	///  The card suit, <code>null</code> for wizards and fools.
	Suit? suit;

	///  The value of the card.
	Value value;

	/// Creates a Card.
	Card({
			this.suit, 
			this.value = Value.n, 
	});

	/// Parses a Card from a string source.
	static Card? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a Card instance from the given reader.
	static Card read(JsonReader json) {
		Card result = Card();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "Card";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "suit": {
				suit = json.tryNull() ? null : readSuit(json);
				break;
			}
			case "value": {
				value = readValue(json);
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		var _suit = suit;
		if (_suit != null) {
			json.addKey("suit");
			writeSuit(json, _suit);
		}

		json.addKey("value");
		writeValue(json, value);
	}

}

/// Visitor interface for Cmd.
abstract class CmdVisitor<R, A> implements LoginCmdVisitor<R, A>, GameCmdVisitor<R, A> {
	R visitListGames(ListGames self, A arg);
	R visitJoinGame(JoinGame self, A arg);
	R visitCreateGame(CreateGame self, A arg);
	R visitStartGame(StartGame self, A arg);
	R visitLeaveGame(LeaveGame self, A arg);
}

///  A message that is sent from a client to the game server to trigger some action.
abstract class Cmd extends _JsonObject {
	/// Creates a Cmd.
	Cmd();

	/// Parses a Cmd from a string source.
	static Cmd? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a Cmd instance from the given reader.
	static Cmd? read(JsonReader json) {
		Cmd? result;

		json.expectArray();
		if (!json.hasNext()) {
			return null;
		}

		switch (json.expectString()) {
			case "ListGames": result = ListGames(); break;
			case "JoinGame": result = JoinGame(); break;
			case "CreateGame": result = CreateGame(); break;
			case "StartGame": result = StartGame(); break;
			case "LeaveGame": result = LeaveGame(); break;
			case "Hello": result = Hello(); break;
			case "CreateAccount": result = CreateAccount(); break;
			case "AddEmail": result = AddEmail(); break;
			case "VerifyEmail": result = VerifyEmail(); break;
			case "Login": result = Login(); break;
			case "Reconnect": result = Reconnect(); break;
			case "SelectTrump": result = SelectTrump(); break;
			case "Bid": result = Bid(); break;
			case "Lead": result = Lead(); break;
			case "ConfirmTrick": result = ConfirmTrick(); break;
			case "ConfirmRound": result = ConfirmRound(); break;
			default: result = null;
		}

		if (!json.hasNext() || json.tryNull()) {
			return null;
		}

		if (result == null) {
			json.skipAnyValue();
		} else {
			result._readContent(json);
		}
		json.endArray();

		return result;
	}

	R visitCmd<R, A>(CmdVisitor<R, A> v, A arg);

}

/// Visitor interface for Msg.
abstract class MsgVisitor<R, A> implements ResultMsgVisitor<R, A>, GameMsgVisitor<R, A> {
	R visitGameCreated(GameCreated self, A arg);
	R visitGameDeleted(GameDeleted self, A arg);
	R visitGameStarted(GameStarted self, A arg);
	R visitJoinAnnounce(JoinAnnounce self, A arg);
	R visitLeaveAnnounce(LeaveAnnounce self, A arg);
}

///  A message sent from the game server to active clients.
abstract class Msg extends _JsonObject {
	/// Creates a Msg.
	Msg();

	/// Parses a Msg from a string source.
	static Msg? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a Msg instance from the given reader.
	static Msg? read(JsonReader json) {
		Msg? result;

		json.expectArray();
		if (!json.hasNext()) {
			return null;
		}

		switch (json.expectString()) {
			case "GameCreated": result = GameCreated(); break;
			case "GameDeleted": result = GameDeleted(); break;
			case "GameStarted": result = GameStarted(); break;
			case "JoinAnnounce": result = JoinAnnounce(); break;
			case "LeaveAnnounce": result = LeaveAnnounce(); break;
			case "Error": result = Error(); break;
			case "HelloResult": result = HelloResult(); break;
			case "CreateAccountResult": result = CreateAccountResult(); break;
			case "AddEmailSuccess": result = AddEmailSuccess(); break;
			case "VerifyEmailSuccess": result = VerifyEmailSuccess(); break;
			case "Welcome": result = Welcome(); break;
			case "LoginFailed": result = LoginFailed(); break;
			case "ListGamesResult": result = ListGamesResult(); break;
			case "StartRound": result = StartRound(); break;
			case "RequestTrumpSelection": result = RequestTrumpSelection(); break;
			case "StartBids": result = StartBids(); break;
			case "RequestBid": result = RequestBid(); break;
			case "StartLead": result = StartLead(); break;
			case "RequestLead": result = RequestLead(); break;
			case "FinishTurn": result = FinishTurn(); break;
			case "FinishRound": result = FinishRound(); break;
			case "FinishGame": result = FinishGame(); break;
			case "Announce": result = Announce(); break;
			default: result = null;
		}

		if (!json.hasNext() || json.tryNull()) {
			return null;
		}

		if (result == null) {
			json.skipAnyValue();
		} else {
			result._readContent(json);
		}
		json.endArray();

		return result;
	}

	R visitMsg<R, A>(MsgVisitor<R, A> v, A arg);

}

/// Visitor interface for ResultMsg.
abstract class ResultMsgVisitor<R, A> {
	R visitError(Error self, A arg);
	R visitHelloResult(HelloResult self, A arg);
	R visitCreateAccountResult(CreateAccountResult self, A arg);
	R visitAddEmailSuccess(AddEmailSuccess self, A arg);
	R visitVerifyEmailSuccess(VerifyEmailSuccess self, A arg);
	R visitWelcome(Welcome self, A arg);
	R visitLoginFailed(LoginFailed self, A arg);
	R visitListGamesResult(ListGamesResult self, A arg);
}

///  A message that is sent in direct result to a {@link Cmd}.
abstract class ResultMsg extends Msg {
	/// Creates a ResultMsg.
	ResultMsg();

	/// Parses a ResultMsg from a string source.
	static ResultMsg? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a ResultMsg instance from the given reader.
	static ResultMsg? read(JsonReader json) {
		ResultMsg? result;

		json.expectArray();
		if (!json.hasNext()) {
			return null;
		}

		switch (json.expectString()) {
			case "Error": result = Error(); break;
			case "HelloResult": result = HelloResult(); break;
			case "CreateAccountResult": result = CreateAccountResult(); break;
			case "AddEmailSuccess": result = AddEmailSuccess(); break;
			case "VerifyEmailSuccess": result = VerifyEmailSuccess(); break;
			case "Welcome": result = Welcome(); break;
			case "LoginFailed": result = LoginFailed(); break;
			case "ListGamesResult": result = ListGamesResult(); break;
			default: result = null;
		}

		if (!json.hasNext() || json.tryNull()) {
			return null;
		}

		if (result == null) {
			json.skipAnyValue();
		} else {
			result._readContent(json);
		}
		json.endArray();

		return result;
	}

	R visitResultMsg<R, A>(ResultMsgVisitor<R, A> v, A arg);

	@override
	R visitMsg<R, A>(MsgVisitor<R, A> v, A arg) => visitResultMsg(v, arg);

}

///  Informs the client that the last {@link Cmd} failed.
class Error extends ResultMsg {
	///  Description of the error that has been detected.
	String message;

	/// Creates a Error.
	Error({
			this.message = "", 
	});

	/// Parses a Error from a string source.
	static Error? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a Error instance from the given reader.
	static Error read(JsonReader json) {
		Error result = Error();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "Error";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "message": {
				message = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("message");
		json.addString(message);
	}

	@override
	R visitResultMsg<R, A>(ResultMsgVisitor<R, A> v, A arg) => v.visitError(this, arg);

}

/// Visitor interface for LoginCmd.
abstract class LoginCmdVisitor<R, A> {
	R visitHello(Hello self, A arg);
	R visitCreateAccount(CreateAccount self, A arg);
	R visitAddEmail(AddEmail self, A arg);
	R visitVerifyEmail(VerifyEmail self, A arg);
	R visitLogin(Login self, A arg);
	R visitReconnect(Reconnect self, A arg);
}

///  Base class for commands for account management.
abstract class LoginCmd extends Cmd {
	/// Creates a LoginCmd.
	LoginCmd();

	/// Parses a LoginCmd from a string source.
	static LoginCmd? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a LoginCmd instance from the given reader.
	static LoginCmd? read(JsonReader json) {
		LoginCmd? result;

		json.expectArray();
		if (!json.hasNext()) {
			return null;
		}

		switch (json.expectString()) {
			case "Hello": result = Hello(); break;
			case "CreateAccount": result = CreateAccount(); break;
			case "AddEmail": result = AddEmail(); break;
			case "VerifyEmail": result = VerifyEmail(); break;
			case "Login": result = Login(); break;
			case "Reconnect": result = Reconnect(); break;
			default: result = null;
		}

		if (!json.hasNext() || json.tryNull()) {
			return null;
		}

		if (result == null) {
			json.skipAnyValue();
		} else {
			result._readContent(json);
		}
		json.endArray();

		return result;
	}

	R visitLoginCmd<R, A>(LoginCmdVisitor<R, A> v, A arg);

	@override
	R visitCmd<R, A>(CmdVisitor<R, A> v, A arg) => visitLoginCmd(v, arg);

}

///  The first method sent after opening a connection.
class Hello extends LoginCmd {
	///  The protocol version of the connecting client. This can be used to detect version mismatch of client and server.
	int version;

	///  The language the server should talk to the user.
	String language;

	/// Creates a Hello.
	Hello({
			this.version = 0, 
			this.language = "", 
	});

	/// Parses a Hello from a string source.
	static Hello? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a Hello instance from the given reader.
	static Hello read(JsonReader json) {
		Hello result = Hello();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "Hello";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "version": {
				version = json.expectInt();
				break;
			}
			case "language": {
				language = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("version");
		json.addNumber(version);

		json.addKey("language");
		json.addString(language);
	}

	@override
	R visitLoginCmd<R, A>(LoginCmdVisitor<R, A> v, A arg) => v.visitHello(this, arg);

}

///  Answer to {@link Hello}.
class HelloResult extends ResultMsg {
	///  Whether protocol version matches and login can continue.
	bool ok;

	///  The protocol version of the server.
	int version;

	///  Description of the problem, if not ok.
	String msg;

	/// Creates a HelloResult.
	HelloResult({
			this.ok = false, 
			this.version = 0, 
			this.msg = "", 
	});

	/// Parses a HelloResult from a string source.
	static HelloResult? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a HelloResult instance from the given reader.
	static HelloResult read(JsonReader json) {
		HelloResult result = HelloResult();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "HelloResult";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "ok": {
				ok = json.expectBool();
				break;
			}
			case "version": {
				version = json.expectInt();
				break;
			}
			case "msg": {
				msg = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("ok");
		json.addBool(ok);

		json.addKey("version");
		json.addNumber(version);

		json.addKey("msg");
		json.addString(msg);
	}

	@override
	R visitResultMsg<R, A>(ResultMsgVisitor<R, A> v, A arg) => v.visitHelloResult(this, arg);

}

///  Command requesting the creation of a new account. Result is either {@link CreateAccountResult} on success, or {@link Error} on error.
class CreateAccount extends LoginCmd {
	///  The nickname for the newly created account.
	String nickname;

	/// Creates a CreateAccount.
	CreateAccount({
			this.nickname = "", 
	});

	/// Parses a CreateAccount from a string source.
	static CreateAccount? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a CreateAccount instance from the given reader.
	static CreateAccount read(JsonReader json) {
		CreateAccount result = CreateAccount();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "CreateAccount";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "nickname": {
				nickname = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("nickname");
		json.addString(nickname);
	}

	@override
	R visitLoginCmd<R, A>(LoginCmdVisitor<R, A> v, A arg) => v.visitCreateAccount(this, arg);

}

///  Result of {@link CreateAccount}, if successful.
class CreateAccountResult extends ResultMsg {
	///  The user ID of the newly created account.
	String uid;

	///  The login credentials to use for the newly created account.
	String secret;

	/// Creates a CreateAccountResult.
	CreateAccountResult({
			this.uid = "", 
			this.secret = "", 
	});

	/// Parses a CreateAccountResult from a string source.
	static CreateAccountResult? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a CreateAccountResult instance from the given reader.
	static CreateAccountResult read(JsonReader json) {
		CreateAccountResult result = CreateAccountResult();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "CreateAccountResult";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "uid": {
				uid = json.expectString();
				break;
			}
			case "secret": {
				secret = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("uid");
		json.addString(uid);

		json.addKey("secret");
		json.addString(secret);
	}

	@override
	R visitResultMsg<R, A>(ResultMsgVisitor<R, A> v, A arg) => v.visitCreateAccountResult(this, arg);

}

///  Command to assign an email to an existing account. An e-mail is send to the provided address with a verification token that must be provided to {@link VerifyEmail} in a following request.
class AddEmail extends LoginCmd {
	///  The user ID, see {@link CreateAccountResult#uid}.
	String uid;

	///  The user's login credentials, see {@link CreateAccountResult#secret}.
	String secret;

	///  The e-mail address to assign to the user's account.
	String email;

	/// Creates a AddEmail.
	AddEmail({
			this.uid = "", 
			this.secret = "", 
			this.email = "", 
	});

	/// Parses a AddEmail from a string source.
	static AddEmail? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a AddEmail instance from the given reader.
	static AddEmail read(JsonReader json) {
		AddEmail result = AddEmail();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "AddEmail";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "uid": {
				uid = json.expectString();
				break;
			}
			case "secret": {
				secret = json.expectString();
				break;
			}
			case "email": {
				email = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("uid");
		json.addString(uid);

		json.addKey("secret");
		json.addString(secret);

		json.addKey("email");
		json.addString(email);
	}

	@override
	R visitLoginCmd<R, A>(LoginCmdVisitor<R, A> v, A arg) => v.visitAddEmail(this, arg);

}

///  Acknowledges adding the e-mail.
class AddEmailSuccess extends ResultMsg {
	/// Creates a AddEmailSuccess.
	AddEmailSuccess();

	/// Parses a AddEmailSuccess from a string source.
	static AddEmailSuccess? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a AddEmailSuccess instance from the given reader.
	static AddEmailSuccess read(JsonReader json) {
		AddEmailSuccess result = AddEmailSuccess();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "AddEmailSuccess";

	@override
	R visitResultMsg<R, A>(ResultMsgVisitor<R, A> v, A arg) => v.visitAddEmailSuccess(this, arg);

}

///  Command that verifies the e-mail address added to an account with {@link AddEmail}.
class VerifyEmail extends LoginCmd {
	///  The user ID, see {@link CreateAccountResult#uid}.
	String uid;

	///  The verification token that was sent to the e-mail address.
	String token;

	/// Creates a VerifyEmail.
	VerifyEmail({
			this.uid = "", 
			this.token = "", 
	});

	/// Parses a VerifyEmail from a string source.
	static VerifyEmail? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a VerifyEmail instance from the given reader.
	static VerifyEmail read(JsonReader json) {
		VerifyEmail result = VerifyEmail();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "VerifyEmail";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "uid": {
				uid = json.expectString();
				break;
			}
			case "token": {
				token = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("uid");
		json.addString(uid);

		json.addKey("token");
		json.addString(token);
	}

	@override
	R visitLoginCmd<R, A>(LoginCmdVisitor<R, A> v, A arg) => v.visitVerifyEmail(this, arg);

}

///  Acknowledges the e-mail verification.
class VerifyEmailSuccess extends ResultMsg {
	/// Creates a VerifyEmailSuccess.
	VerifyEmailSuccess();

	/// Parses a VerifyEmailSuccess from a string source.
	static VerifyEmailSuccess? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a VerifyEmailSuccess instance from the given reader.
	static VerifyEmailSuccess read(JsonReader json) {
		VerifyEmailSuccess result = VerifyEmailSuccess();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "VerifyEmailSuccess";

	@override
	R visitResultMsg<R, A>(ResultMsgVisitor<R, A> v, A arg) => v.visitVerifyEmailSuccess(this, arg);

}

///  First message that must be sent after connecting to a game server.
/// 
///  <p>
///  On success, a {@link Welcome} message is sent back.
///  </p>
class Login extends LoginCmd {
	///  The UID of the player.
	String uid;

	///  The user's login secret.
	String secret;

	/// Creates a Login.
	Login({
			this.uid = "", 
			this.secret = "", 
	});

	/// Parses a Login from a string source.
	static Login? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a Login instance from the given reader.
	static Login read(JsonReader json) {
		Login result = Login();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "Login";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "uid": {
				uid = json.expectString();
				break;
			}
			case "secret": {
				secret = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("uid");
		json.addString(uid);

		json.addKey("secret");
		json.addString(secret);
	}

	@override
	R visitLoginCmd<R, A>(LoginCmdVisitor<R, A> v, A arg) => v.visitLogin(this, arg);

}

///  Response message upon successful {@link Login}.
class Welcome extends ResultMsg {
	///  The ID of the player that logged in to the server.
	String playerId;

	/// Creates a Welcome.
	Welcome({
			this.playerId = "", 
	});

	/// Parses a Welcome from a string source.
	static Welcome? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a Welcome instance from the given reader.
	static Welcome read(JsonReader json) {
		Welcome result = Welcome();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "Welcome";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "playerId": {
				playerId = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("playerId");
		json.addString(playerId);
	}

	@override
	R visitResultMsg<R, A>(ResultMsgVisitor<R, A> v, A arg) => v.visitWelcome(this, arg);

}

///  Sent, when {@link Login} is not successful.
class LoginFailed extends ResultMsg {
	///  Further description of the problem
	String msg;

	/// Creates a LoginFailed.
	LoginFailed({
			this.msg = "", 
	});

	/// Parses a LoginFailed from a string source.
	static LoginFailed? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a LoginFailed instance from the given reader.
	static LoginFailed read(JsonReader json) {
		LoginFailed result = LoginFailed();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "LoginFailed";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "msg": {
				msg = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("msg");
		json.addString(msg);
	}

	@override
	R visitResultMsg<R, A>(ResultMsgVisitor<R, A> v, A arg) => v.visitLoginFailed(this, arg);

}

///  Takes over a lost connection that was established before.
class Reconnect extends LoginCmd {
	///  The ID that was assigned to the player in a former {@link Welcome} message.
	String playerId;

	///  The ID of the game that this player would like to reconnect to.
	String gameId;

	/// Creates a Reconnect.
	Reconnect({
			this.playerId = "", 
			this.gameId = "", 
	});

	/// Parses a Reconnect from a string source.
	static Reconnect? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a Reconnect instance from the given reader.
	static Reconnect read(JsonReader json) {
		Reconnect result = Reconnect();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "Reconnect";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "playerId": {
				playerId = json.expectString();
				break;
			}
			case "gameId": {
				gameId = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("playerId");
		json.addString(playerId);

		json.addKey("gameId");
		json.addString(gameId);
	}

	@override
	R visitLoginCmd<R, A>(LoginCmdVisitor<R, A> v, A arg) => v.visitReconnect(this, arg);

}

///  Requests a listing of games waiting for player.
class ListGames extends Cmd {
	/// Creates a ListGames.
	ListGames();

	/// Parses a ListGames from a string source.
	static ListGames? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a ListGames instance from the given reader.
	static ListGames read(JsonReader json) {
		ListGames result = ListGames();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "ListGames";

	@override
	R visitCmd<R, A>(CmdVisitor<R, A> v, A arg) => v.visitListGames(this, arg);

}

///  Provides a listing of games waiting for players.
class ListGamesResult extends ResultMsg {
	///  List of all games on the server currently accepting new players.
	List<Game> games;

	/// Creates a ListGamesResult.
	ListGamesResult({
			this.games = const [], 
	});

	/// Parses a ListGamesResult from a string source.
	static ListGamesResult? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a ListGamesResult instance from the given reader.
	static ListGamesResult read(JsonReader json) {
		ListGamesResult result = ListGamesResult();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "ListGamesResult";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "games": {
				json.expectArray();
				games = [];
				while (json.hasNext()) {
					if (!json.tryNull()) {
						games.add(Game.read(json));
					}
				}
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("games");
		json.startArray();
		for (var _element in games) {
			_element.writeContent(json);
		}
		json.endArray();
	}

	@override
	R visitResultMsg<R, A>(ResultMsgVisitor<R, A> v, A arg) => v.visitListGamesResult(this, arg);

}

///  Requests joining a game.
class JoinGame extends Cmd {
	///  The ID of the game the sender wants to join.
	String gameId;

	/// Creates a JoinGame.
	JoinGame({
			this.gameId = "", 
	});

	/// Parses a JoinGame from a string source.
	static JoinGame? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a JoinGame instance from the given reader.
	static JoinGame read(JsonReader json) {
		JoinGame result = JoinGame();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "JoinGame";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "gameId": {
				gameId = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("gameId");
		json.addString(gameId);
	}

	@override
	R visitCmd<R, A>(CmdVisitor<R, A> v, A arg) => v.visitJoinGame(this, arg);

}

///  Command that requests creating a new game on the server.
///  <p>
///  On success, a {@link GameCreated} response message is sent to all clients not currently participating in a game.
///  </p>
class CreateGame extends Cmd {
	/// Creates a CreateGame.
	CreateGame();

	/// Parses a CreateGame from a string source.
	static CreateGame? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a CreateGame instance from the given reader.
	static CreateGame read(JsonReader json) {
		CreateGame result = CreateGame();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "CreateGame";

	@override
	R visitCmd<R, A>(CmdVisitor<R, A> v, A arg) => v.visitCreateGame(this, arg);

}

///  Informs idle players that a new game was created.
class GameCreated extends Msg {
	///  Id of the player that created the game. This player is implicitly the first player in the game without explicitly joining the game.
	String ownerId;

	///  The newly created game.
	Game? game;

	/// Creates a GameCreated.
	GameCreated({
			this.ownerId = "", 
			this.game, 
	});

	/// Parses a GameCreated from a string source.
	static GameCreated? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a GameCreated instance from the given reader.
	static GameCreated read(JsonReader json) {
		GameCreated result = GameCreated();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "GameCreated";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "ownerId": {
				ownerId = json.expectString();
				break;
			}
			case "game": {
				game = json.tryNull() ? null : Game.read(json);
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("ownerId");
		json.addString(ownerId);

		var _game = game;
		if (_game != null) {
			json.addKey("game");
			_game.writeContent(json);
		}
	}

	@override
	R visitMsg<R, A>(MsgVisitor<R, A> v, A arg) => v.visitGameCreated(this, arg);

}

///  Informs idle players that a game that was waiting for players has been deleted.
class GameDeleted extends Msg {
	///  The ID of the game that has been deleted.
	String gameId;

	/// Creates a GameDeleted.
	GameDeleted({
			this.gameId = "", 
	});

	/// Parses a GameDeleted from a string source.
	static GameDeleted? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a GameDeleted instance from the given reader.
	static GameDeleted read(JsonReader json) {
		GameDeleted result = GameDeleted();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "GameDeleted";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "gameId": {
				gameId = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("gameId");
		json.addString(gameId);
	}

	@override
	R visitMsg<R, A>(MsgVisitor<R, A> v, A arg) => v.visitGameDeleted(this, arg);

}

///  Informs members of a game and idle players that a game has started and is not accepting players anymore.
class StartGame extends Cmd {
	///  The ID of the game that has started.
	String gameId;

	/// Creates a StartGame.
	StartGame({
			this.gameId = "", 
	});

	/// Parses a StartGame from a string source.
	static StartGame? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a StartGame instance from the given reader.
	static StartGame read(JsonReader json) {
		StartGame result = StartGame();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "StartGame";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "gameId": {
				gameId = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("gameId");
		json.addString(gameId);
	}

	@override
	R visitCmd<R, A>(CmdVisitor<R, A> v, A arg) => v.visitStartGame(this, arg);

}

///  Message sent in response to {@link StartGame} to announce the final game configuration.
class GameStarted extends Msg {
	///  The final settings of the game that is started
	Game? game;

	/// Creates a GameStarted.
	GameStarted({
			this.game, 
	});

	/// Parses a GameStarted from a string source.
	static GameStarted? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a GameStarted instance from the given reader.
	static GameStarted read(JsonReader json) {
		GameStarted result = GameStarted();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "GameStarted";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "game": {
				game = json.tryNull() ? null : Game.read(json);
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		var _game = game;
		if (_game != null) {
			json.addKey("game");
			_game.writeContent(json);
		}
	}

	@override
	R visitMsg<R, A>(MsgVisitor<R, A> v, A arg) => v.visitGameStarted(this, arg);

}

///  Announces that a player has joined a game.
class JoinAnnounce extends Msg {
	///  The player that joined the game.
	Player? player;

	///  The ID of the game the player joined.
	String gameId;

	/// Creates a JoinAnnounce.
	JoinAnnounce({
			this.player, 
			this.gameId = "", 
	});

	/// Parses a JoinAnnounce from a string source.
	static JoinAnnounce? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a JoinAnnounce instance from the given reader.
	static JoinAnnounce read(JsonReader json) {
		JoinAnnounce result = JoinAnnounce();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "JoinAnnounce";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "player": {
				player = json.tryNull() ? null : Player.read(json);
				break;
			}
			case "gameId": {
				gameId = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		var _player = player;
		if (_player != null) {
			json.addKey("player");
			_player.writeContent(json);
		}

		json.addKey("gameId");
		json.addString(gameId);
	}

	@override
	R visitMsg<R, A>(MsgVisitor<R, A> v, A arg) => v.visitJoinAnnounce(this, arg);

}

///  Announces that a player has left a game.
class LeaveAnnounce extends Msg {
	///  The ID of the player that left a game.
	String playerId;

	///  The ID of the game the player left.
	String gameId;

	/// Creates a LeaveAnnounce.
	LeaveAnnounce({
			this.playerId = "", 
			this.gameId = "", 
	});

	/// Parses a LeaveAnnounce from a string source.
	static LeaveAnnounce? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a LeaveAnnounce instance from the given reader.
	static LeaveAnnounce read(JsonReader json) {
		LeaveAnnounce result = LeaveAnnounce();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "LeaveAnnounce";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "playerId": {
				playerId = json.expectString();
				break;
			}
			case "gameId": {
				gameId = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("playerId");
		json.addString(playerId);

		json.addKey("gameId");
		json.addString(gameId);
	}

	@override
	R visitMsg<R, A>(MsgVisitor<R, A> v, A arg) => v.visitLeaveAnnounce(this, arg);

}

/// Visitor interface for GameCmd.
abstract class GameCmdVisitor<R, A> {
	R visitSelectTrump(SelectTrump self, A arg);
	R visitBid(Bid self, A arg);
	R visitLead(Lead self, A arg);
	R visitConfirmTrick(ConfirmTrick self, A arg);
	R visitConfirmRound(ConfirmRound self, A arg);
}

///  A {@link Cmd} targeting a running game.
abstract class GameCmd extends Cmd {
	/// Creates a GameCmd.
	GameCmd();

	/// Parses a GameCmd from a string source.
	static GameCmd? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a GameCmd instance from the given reader.
	static GameCmd? read(JsonReader json) {
		GameCmd? result;

		json.expectArray();
		if (!json.hasNext()) {
			return null;
		}

		switch (json.expectString()) {
			case "SelectTrump": result = SelectTrump(); break;
			case "Bid": result = Bid(); break;
			case "Lead": result = Lead(); break;
			case "ConfirmTrick": result = ConfirmTrick(); break;
			case "ConfirmRound": result = ConfirmRound(); break;
			default: result = null;
		}

		if (!json.hasNext() || json.tryNull()) {
			return null;
		}

		if (result == null) {
			json.skipAnyValue();
		} else {
			result._readContent(json);
		}
		json.endArray();

		return result;
	}

	R visitGameCmd<R, A>(GameCmdVisitor<R, A> v, A arg);

	@override
	R visitCmd<R, A>(CmdVisitor<R, A> v, A arg) => visitGameCmd(v, arg);

}

/// Visitor interface for GameMsg.
abstract class GameMsgVisitor<R, A> {
	R visitStartRound(StartRound self, A arg);
	R visitRequestTrumpSelection(RequestTrumpSelection self, A arg);
	R visitStartBids(StartBids self, A arg);
	R visitRequestBid(RequestBid self, A arg);
	R visitStartLead(StartLead self, A arg);
	R visitRequestLead(RequestLead self, A arg);
	R visitFinishTurn(FinishTurn self, A arg);
	R visitFinishRound(FinishRound self, A arg);
	R visitFinishGame(FinishGame self, A arg);
	R visitAnnounce(Announce self, A arg);
}

///  A {@link Msg} targeting a running game and is only interesting for players of that game.
abstract class GameMsg extends Msg {
	/// Creates a GameMsg.
	GameMsg();

	/// Parses a GameMsg from a string source.
	static GameMsg? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a GameMsg instance from the given reader.
	static GameMsg? read(JsonReader json) {
		GameMsg? result;

		json.expectArray();
		if (!json.hasNext()) {
			return null;
		}

		switch (json.expectString()) {
			case "StartRound": result = StartRound(); break;
			case "RequestTrumpSelection": result = RequestTrumpSelection(); break;
			case "StartBids": result = StartBids(); break;
			case "RequestBid": result = RequestBid(); break;
			case "StartLead": result = StartLead(); break;
			case "RequestLead": result = RequestLead(); break;
			case "FinishTurn": result = FinishTurn(); break;
			case "FinishRound": result = FinishRound(); break;
			case "FinishGame": result = FinishGame(); break;
			case "Announce": result = Announce(); break;
			default: result = null;
		}

		if (!json.hasNext() || json.tryNull()) {
			return null;
		}

		if (result == null) {
			json.skipAnyValue();
		} else {
			result._readContent(json);
		}
		json.endArray();

		return result;
	}

	R visitGameMsg<R, A>(GameMsgVisitor<R, A> v, A arg);

	@override
	R visitMsg<R, A>(MsgVisitor<R, A> v, A arg) => visitGameMsg(v, arg);

}

///  Starts a round by assigning cards to players.
class StartRound extends GameMsg {
	///  The number of the round. 1 is the first round.
	int round;

	///  The number of rounds being played in this game (depends on the number of players).
	int maxRound;

	///  The ID of the player that must make the first bid and leads the first card.
	String startPlayer;

	///  The players and their order in this round.
	List<Player> players;

	///  The cards given to the player that receives this message.
	List<Card> cards;

	///  The given trump card. In the last round, there is no trump card.
	Card? trumpCard;

	/// Creates a StartRound.
	StartRound({
			this.round = 0, 
			this.maxRound = 0, 
			this.startPlayer = "", 
			this.players = const [], 
			this.cards = const [], 
			this.trumpCard, 
	});

	/// Parses a StartRound from a string source.
	static StartRound? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a StartRound instance from the given reader.
	static StartRound read(JsonReader json) {
		StartRound result = StartRound();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "StartRound";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "round": {
				round = json.expectInt();
				break;
			}
			case "maxRound": {
				maxRound = json.expectInt();
				break;
			}
			case "startPlayer": {
				startPlayer = json.expectString();
				break;
			}
			case "players": {
				json.expectArray();
				players = [];
				while (json.hasNext()) {
					if (!json.tryNull()) {
						players.add(Player.read(json));
					}
				}
				break;
			}
			case "cards": {
				json.expectArray();
				cards = [];
				while (json.hasNext()) {
					if (!json.tryNull()) {
						cards.add(Card.read(json));
					}
				}
				break;
			}
			case "trumpCard": {
				trumpCard = json.tryNull() ? null : Card.read(json);
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("round");
		json.addNumber(round);

		json.addKey("maxRound");
		json.addNumber(maxRound);

		json.addKey("startPlayer");
		json.addString(startPlayer);

		json.addKey("players");
		json.startArray();
		for (var _element in players) {
			_element.writeContent(json);
		}
		json.endArray();

		json.addKey("cards");
		json.startArray();
		for (var _element in cards) {
			_element.writeContent(json);
		}
		json.endArray();

		var _trumpCard = trumpCard;
		if (_trumpCard != null) {
			json.addKey("trumpCard");
			_trumpCard.writeContent(json);
		}
	}

	@override
	R visitGameMsg<R, A>(GameMsgVisitor<R, A> v, A arg) => v.visitStartRound(this, arg);

}

///  Requests a player to select the trump suit in case the trump card was a wizard.
class RequestTrumpSelection extends GameMsg {
	///  The ID of the player that is expected to select the trump suit.
	String playerId;

	/// Creates a RequestTrumpSelection.
	RequestTrumpSelection({
			this.playerId = "", 
	});

	/// Parses a RequestTrumpSelection from a string source.
	static RequestTrumpSelection? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a RequestTrumpSelection instance from the given reader.
	static RequestTrumpSelection read(JsonReader json) {
		RequestTrumpSelection result = RequestTrumpSelection();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "RequestTrumpSelection";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "playerId": {
				playerId = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("playerId");
		json.addString(playerId);
	}

	@override
	R visitGameMsg<R, A>(GameMsgVisitor<R, A> v, A arg) => v.visitRequestTrumpSelection(this, arg);

}

///  {@link GameCmd} to select the trump suit in response to {@link RequestTrumpSelection}.
class SelectTrump extends GameCmd {
	///  The trump suit selected.
	Suit trumpSuit;

	/// Creates a SelectTrump.
	SelectTrump({
			this.trumpSuit = Suit.diamond, 
	});

	/// Parses a SelectTrump from a string source.
	static SelectTrump? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a SelectTrump instance from the given reader.
	static SelectTrump read(JsonReader json) {
		SelectTrump result = SelectTrump();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "SelectTrump";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "trumpSuit": {
				trumpSuit = readSuit(json);
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("trumpSuit");
		writeSuit(json, trumpSuit);
	}

	@override
	R visitGameCmd<R, A>(GameCmdVisitor<R, A> v, A arg) => v.visitSelectTrump(this, arg);

}

///  Message that starts the bid phase.
class StartBids extends GameMsg {
	/// Creates a StartBids.
	StartBids();

	/// Parses a StartBids from a string source.
	static StartBids? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a StartBids instance from the given reader.
	static StartBids read(JsonReader json) {
		StartBids result = StartBids();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "StartBids";

	@override
	R visitGameMsg<R, A>(GameMsgVisitor<R, A> v, A arg) => v.visitStartBids(this, arg);

}

///  Requests a bid.
class RequestBid extends GameMsg {
	///  The player that is expected to place a bid.
	String playerId;

	///  The number of tricks expected by other players so far.
	int expected;

	///  The round number and maximum number of tricks possible in that round.
	int round;

	/// Creates a RequestBid.
	RequestBid({
			this.playerId = "", 
			this.expected = 0, 
			this.round = 0, 
	});

	/// Parses a RequestBid from a string source.
	static RequestBid? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a RequestBid instance from the given reader.
	static RequestBid read(JsonReader json) {
		RequestBid result = RequestBid();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "RequestBid";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "playerId": {
				playerId = json.expectString();
				break;
			}
			case "expected": {
				expected = json.expectInt();
				break;
			}
			case "round": {
				round = json.expectInt();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("playerId");
		json.addString(playerId);

		json.addKey("expected");
		json.addNumber(expected);

		json.addKey("round");
		json.addNumber(round);
	}

	@override
	R visitGameMsg<R, A>(GameMsgVisitor<R, A> v, A arg) => v.visitRequestBid(this, arg);

}

///  Message announcing a player's bid.
class Bid extends GameCmd {
	///  The bid of the current player
	int cnt;

	///  The sum of tricks expected by all players so far.
	/// 
	///  <p>
	///  The value is only relevant when the message is announced to all players. It is not required to set this value when sending the command to the server.
	///  </p>
	int expected;

	/// Creates a Bid.
	Bid({
			this.cnt = 0, 
			this.expected = 0, 
	});

	/// Parses a Bid from a string source.
	static Bid? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a Bid instance from the given reader.
	static Bid read(JsonReader json) {
		Bid result = Bid();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "Bid";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "cnt": {
				cnt = json.expectInt();
				break;
			}
			case "expected": {
				expected = json.expectInt();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("cnt");
		json.addNumber(cnt);

		json.addKey("expected");
		json.addNumber(expected);
	}

	@override
	R visitGameCmd<R, A>(GameCmdVisitor<R, A> v, A arg) => v.visitBid(this, arg);

}

///  Message announcing that all bids are placed.
class StartLead extends GameMsg {
	///  Score information of all players by their IDs.
	Map<String, PlayerInfo> state;

	///  The cards played so far. Only relevant when reconnecting to a game.
	List<PlayedCard> currentTrick;

	/// Creates a StartLead.
	StartLead({
			this.state = const {}, 
			this.currentTrick = const [], 
	});

	/// Parses a StartLead from a string source.
	static StartLead? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a StartLead instance from the given reader.
	static StartLead read(JsonReader json) {
		StartLead result = StartLead();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "StartLead";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "state": {
				Map<String, PlayerInfo> _state = {};
				json.expectObject();
				while (json.hasNextKey()) {
					String __key = json.nextKey()!;
					if (!json.tryNull()) {
						_state[__key] = PlayerInfo.read(json);
					}
				}
				state = _state;
				break;
			}
			case "currentTrick": {
				json.expectArray();
				currentTrick = [];
				while (json.hasNext()) {
					if (!json.tryNull()) {
						currentTrick.add(PlayedCard.read(json));
					}
				}
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("state");
		json.startObject();
		for (var x in state.entries) {
			json.addKey(x.key);
			x.value.writeContent(json);
		}
		json.endObject();

		json.addKey("currentTrick");
		json.startArray();
		for (var _element in currentTrick) {
			_element.writeContent(json);
		}
		json.endArray();
	}

	@override
	R visitGameMsg<R, A>(GameMsgVisitor<R, A> v, A arg) => v.visitStartLead(this, arg);

}

///  Info about a {@link Card} played.
class PlayedCard extends _JsonObject {
	///  ID of the player that played the {@link #card}.
	String playerId;

	///  The {@link Card} that lays on the table.
	Card? card;

	/// Creates a PlayedCard.
	PlayedCard({
			this.playerId = "", 
			this.card, 
	});

	/// Parses a PlayedCard from a string source.
	static PlayedCard? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a PlayedCard instance from the given reader.
	static PlayedCard read(JsonReader json) {
		PlayedCard result = PlayedCard();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "PlayedCard";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "playerId": {
				playerId = json.expectString();
				break;
			}
			case "card": {
				card = json.tryNull() ? null : Card.read(json);
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("playerId");
		json.addString(playerId);

		var _card = card;
		if (_card != null) {
			json.addKey("card");
			_card.writeContent(json);
		}
	}

}

///  Score information for a single player, see {@link StartLead}.
class PlayerInfo extends _JsonObject {
	///  The player's bid.
	int bid;

	///  The number of won tricks.
	int tricks;

	///  The current total number of points won by the player so far.
	int total;

	/// Creates a PlayerInfo.
	PlayerInfo({
			this.bid = 0, 
			this.tricks = 0, 
			this.total = 0, 
	});

	/// Parses a PlayerInfo from a string source.
	static PlayerInfo? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a PlayerInfo instance from the given reader.
	static PlayerInfo read(JsonReader json) {
		PlayerInfo result = PlayerInfo();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "PlayerInfo";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "bid": {
				bid = json.expectInt();
				break;
			}
			case "tricks": {
				tricks = json.expectInt();
				break;
			}
			case "points": {
				total = json.expectInt();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("bid");
		json.addNumber(bid);

		json.addKey("tricks");
		json.addNumber(tricks);

		json.addKey("points");
		json.addNumber(total);
	}

}

///  Message sent to all players that announces the player that is about to put a card on the table.
class RequestLead extends GameMsg {
	///  The ID of the player that is expected to send a {@link Lead} message. All other playes are only informed about the player that is in command.
	String playerId;

	/// Creates a RequestLead.
	RequestLead({
			this.playerId = "", 
	});

	/// Parses a RequestLead from a string source.
	static RequestLead? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a RequestLead instance from the given reader.
	static RequestLead read(JsonReader json) {
		RequestLead result = RequestLead();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "RequestLead";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "playerId": {
				playerId = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("playerId");
		json.addString(playerId);
	}

	@override
	R visitGameMsg<R, A>(GameMsgVisitor<R, A> v, A arg) => v.visitRequestLead(this, arg);

}

///  Puts a card on the table.
class Lead extends GameCmd {
	///  The card that is led.
	Card? card;

	/// Creates a Lead.
	Lead({
			this.card, 
	});

	/// Parses a Lead from a string source.
	static Lead? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a Lead instance from the given reader.
	static Lead read(JsonReader json) {
		Lead result = Lead();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "Lead";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "card": {
				card = json.tryNull() ? null : Card.read(json);
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		var _card = card;
		if (_card != null) {
			json.addKey("card");
			_card.writeContent(json);
		}
	}

	@override
	R visitGameCmd<R, A>(GameCmdVisitor<R, A> v, A arg) => v.visitLead(this, arg);

}

///  Message sent at the end of each turn announcing the winner.
class FinishTurn extends GameMsg {
	///  The cards that have been played in this turn.
	List<Card> trick;

	///  The winner of this turn.
	Player? winner;

	/// Creates a FinishTurn.
	FinishTurn({
			this.trick = const [], 
			this.winner, 
	});

	/// Parses a FinishTurn from a string source.
	static FinishTurn? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a FinishTurn instance from the given reader.
	static FinishTurn read(JsonReader json) {
		FinishTurn result = FinishTurn();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "FinishTurn";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "trick": {
				json.expectArray();
				trick = [];
				while (json.hasNext()) {
					if (!json.tryNull()) {
						trick.add(Card.read(json));
					}
				}
				break;
			}
			case "winner": {
				winner = json.tryNull() ? null : Player.read(json);
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("trick");
		json.startArray();
		for (var _element in trick) {
			_element.writeContent(json);
		}
		json.endArray();

		var _winner = winner;
		if (_winner != null) {
			json.addKey("winner");
			_winner.writeContent(json);
		}
	}

	@override
	R visitGameMsg<R, A>(GameMsgVisitor<R, A> v, A arg) => v.visitFinishTurn(this, arg);

}

///  Command in response to {@link FinishTurn} that must be received from all players, before the next turn starts
class ConfirmTrick extends GameCmd {
	/// Creates a ConfirmTrick.
	ConfirmTrick();

	/// Parses a ConfirmTrick from a string source.
	static ConfirmTrick? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a ConfirmTrick instance from the given reader.
	static ConfirmTrick read(JsonReader json) {
		ConfirmTrick result = ConfirmTrick();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "ConfirmTrick";

	@override
	R visitGameCmd<R, A>(GameCmdVisitor<R, A> v, A arg) => v.visitConfirmTrick(this, arg);

}

///  Message sent after each round that announces the points received in this round.
class FinishRound extends GameMsg {
	///  For each player ID the number of points this player wins. The number may be negative.
	Map<String, RoundInfo> info;

	/// Creates a FinishRound.
	FinishRound({
			this.info = const {}, 
	});

	/// Parses a FinishRound from a string source.
	static FinishRound? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a FinishRound instance from the given reader.
	static FinishRound read(JsonReader json) {
		FinishRound result = FinishRound();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "FinishRound";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "info": {
				Map<String, RoundInfo> _info = {};
				json.expectObject();
				while (json.hasNextKey()) {
					String __key = json.nextKey()!;
					if (!json.tryNull()) {
						_info[__key] = RoundInfo.read(json);
					}
				}
				info = _info;
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("info");
		json.startObject();
		for (var x in info.entries) {
			json.addKey(x.key);
			x.value.writeContent(json);
		}
		json.endObject();
	}

	@override
	R visitGameMsg<R, A>(GameMsgVisitor<R, A> v, A arg) => v.visitFinishRound(this, arg);

}

///  Current player status at the end of a round.
class RoundInfo extends _JsonObject {
	///  The points won by the player during the last round. The number may be negative.
	int points;

	///  The total amount of points the player won so far. The number may be negative.
	int total;

	/// Creates a RoundInfo.
	RoundInfo({
			this.points = 0, 
			this.total = 0, 
	});

	/// Parses a RoundInfo from a string source.
	static RoundInfo? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a RoundInfo instance from the given reader.
	static RoundInfo read(JsonReader json) {
		RoundInfo result = RoundInfo();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "RoundInfo";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "points": {
				points = json.expectInt();
				break;
			}
			case "total": {
				total = json.expectInt();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("points");
		json.addNumber(points);

		json.addKey("total");
		json.addNumber(total);
	}

}

///  Command in response to {@link FinishRound} that must be received from all players, before the next round starts
class ConfirmRound extends GameCmd {
	/// Creates a ConfirmRound.
	ConfirmRound();

	/// Parses a ConfirmRound from a string source.
	static ConfirmRound? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a ConfirmRound instance from the given reader.
	static ConfirmRound read(JsonReader json) {
		ConfirmRound result = ConfirmRound();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "ConfirmRound";

	@override
	R visitGameCmd<R, A>(GameCmdVisitor<R, A> v, A arg) => v.visitConfirmRound(this, arg);

}

///  Message sent after the last round of a game.
class FinishGame extends GameMsg {
	///  The total score for each player. The list is ordered by the points the players have won.
	List<PlayerScore> scores;

	/// Creates a FinishGame.
	FinishGame({
			this.scores = const [], 
	});

	/// Parses a FinishGame from a string source.
	static FinishGame? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a FinishGame instance from the given reader.
	static FinishGame read(JsonReader json) {
		FinishGame result = FinishGame();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "FinishGame";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "scores": {
				json.expectArray();
				scores = [];
				while (json.hasNext()) {
					if (!json.tryNull()) {
						scores.add(PlayerScore.read(json));
					}
				}
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("scores");
		json.startArray();
		for (var _element in scores) {
			_element.writeContent(json);
		}
		json.endArray();
	}

	@override
	R visitGameMsg<R, A>(GameMsgVisitor<R, A> v, A arg) => v.visitFinishGame(this, arg);

}

///  Score info for a player.
class PlayerScore extends _JsonObject {
	///  The player.
	Player? player;

	///  The player's points.
	int points;

	/// Creates a PlayerScore.
	PlayerScore({
			this.player, 
			this.points = 0, 
	});

	/// Parses a PlayerScore from a string source.
	static PlayerScore? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a PlayerScore instance from the given reader.
	static PlayerScore read(JsonReader json) {
		PlayerScore result = PlayerScore();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "PlayerScore";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "player": {
				player = json.tryNull() ? null : Player.read(json);
				break;
			}
			case "points": {
				points = json.expectInt();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		var _player = player;
		if (_player != null) {
			json.addKey("player");
			_player.writeContent(json);
		}

		json.addKey("points");
		json.addNumber(points);
	}

}

///  Command to request leaving a game.
class LeaveGame extends Cmd {
	///  The ID of the game to leave.
	String gameId;

	/// Creates a LeaveGame.
	LeaveGame({
			this.gameId = "", 
	});

	/// Parses a LeaveGame from a string source.
	static LeaveGame? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a LeaveGame instance from the given reader.
	static LeaveGame read(JsonReader json) {
		LeaveGame result = LeaveGame();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "LeaveGame";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "gameId": {
				gameId = json.expectString();
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("gameId");
		json.addString(gameId);
	}

	@override
	R visitCmd<R, A>(CmdVisitor<R, A> v, A arg) => v.visitLeaveGame(this, arg);

}

///  Message to forward a {@link GameCmd} sent by one player of a game to all other players.
class Announce extends GameMsg {
	///  The ID of the player that sent the command.
	String playerId;

	///  The command sent by the player with the ID given in {@link #playerId}.
	GameCmd? cmd;

	/// Creates a Announce.
	Announce({
			this.playerId = "", 
			this.cmd, 
	});

	/// Parses a Announce from a string source.
	static Announce? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a Announce instance from the given reader.
	static Announce read(JsonReader json) {
		Announce result = Announce();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "Announce";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "playerId": {
				playerId = json.expectString();
				break;
			}
			case "cmd": {
				cmd = json.tryNull() ? null : GameCmd.read(json);
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("playerId");
		json.addString(playerId);

		var _cmd = cmd;
		if (_cmd != null) {
			json.addKey("cmd");
			_cmd.writeTo(json);
		}
	}

	@override
	R visitGameMsg<R, A>(GameMsgVisitor<R, A> v, A arg) => v.visitAnnounce(this, arg);

}

///  Internal information kept for each player of a game on the server.
class PlayerState extends _JsonObject {
	///  Player data.
	Player? player;

	///  Points won so far.
	int points;

	///  Information about the current round.
	RoundState? roundState;

	/// Creates a PlayerState.
	PlayerState({
			this.player, 
			this.points = 0, 
			this.roundState, 
	});

	/// Parses a PlayerState from a string source.
	static PlayerState? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a PlayerState instance from the given reader.
	static PlayerState read(JsonReader json) {
		PlayerState result = PlayerState();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "PlayerState";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "player": {
				player = json.tryNull() ? null : Player.read(json);
				break;
			}
			case "points": {
				points = json.expectInt();
				break;
			}
			case "roundState": {
				roundState = json.tryNull() ? null : RoundState.read(json);
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		var _player = player;
		if (_player != null) {
			json.addKey("player");
			_player.writeContent(json);
		}

		json.addKey("points");
		json.addNumber(points);

		var _roundState = roundState;
		if (_roundState != null) {
			json.addKey("roundState");
			_roundState.writeContent(json);
		}
	}

}

///  The state of a player as part of {@link PlayerState}.
class RoundState extends _JsonObject {
	///  The player's bid.
	int bidCnt;

	///  The number of tricks won.
	int winCnt;

	///  The cards that the player has currently in his hand.
	List<Card> cards;

	/// Creates a RoundState.
	RoundState({
			this.bidCnt = 0, 
			this.winCnt = 0, 
			this.cards = const [], 
	});

	/// Parses a RoundState from a string source.
	static RoundState? fromString(String source) {
		return read(JsonReader.fromString(source));
	}

	/// Reads a RoundState instance from the given reader.
	static RoundState read(JsonReader json) {
		RoundState result = RoundState();
		result._readContent(json);
		return result;
	}

	@override
	String _jsonType() => "RoundState";

	@override
	void _readProperty(String key, JsonReader json) {
		switch (key) {
			case "bidCnt": {
				bidCnt = json.expectInt();
				break;
			}
			case "winCnt": {
				winCnt = json.expectInt();
				break;
			}
			case "cards": {
				json.expectArray();
				cards = [];
				while (json.hasNext()) {
					if (!json.tryNull()) {
						cards.add(Card.read(json));
					}
				}
				break;
			}
			default: super._readProperty(key, json);
		}
	}

	@override
	void _writeProperties(JsonSink json) {
		super._writeProperties(json);

		json.addKey("bidCnt");
		json.addNumber(bidCnt);

		json.addKey("winCnt");
		json.addNumber(winCnt);

		json.addKey("cards");
		json.startArray();
		for (var _element in cards) {
			_element.writeContent(json);
		}
		json.endArray();
	}

}

