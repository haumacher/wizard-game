
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_ui/msg.dart';

void main() {
  test("Parsing messages", () {
    var data = Login(uid: "Haui", secret: "ABCDEFG").toString();
    print(data);

    var cmd = Cmd.fromString(data);
    print(cmd);

    assert (cmd != null);
    assert (cmd is Login);
    assert ((cmd as Login).uid == "Haui");

    assert (cmd!.visitCmd(CmdTest(), null));

    var msg = Msg.fromString(data);

    // Not a Msg type.
    assert (msg == null);
    print(msg);
  });

  test("Monomorphic references", () {
    var data = GameCreated(ownerId: "Haui", game: Game(gameId: "ID4711", players: [Player(name: "Haui", id: "ID1"), Player(id: "ID2", name: "Other")])).toString();
    print(data);

    var msg = Msg.fromString(data);
    print(msg);

    assert (msg != null);
    assert (msg is GameCreated);
    assert ((msg as GameCreated).game!.players[0].name == "Haui");
  });

}

class CmdTest implements CmdVisitor<bool, void> {
  @override
  bool visitBid(Bid self, void arg) {
    return false;
  }

  @override
  bool visitConfirmRound(ConfirmRound self, void arg) {
    return false;
  }

  @override
  bool visitConfirmTrick(ConfirmTrick self, void arg) {
    return false;
  }

  @override
  bool visitConfirmGame(ConfirmGame self, void arg) {
    return false;
  }

  @override
  bool visitCreateGame(CreateGame self, void arg) {
    return false;
  }

  @override
  bool visitJoinGame(JoinGame self, void arg) {
    return false;
  }

  @override
  bool visitLead(Lead self, void arg) {
    return false;
  }

  @override
  bool visitLeaveGame(LeaveGame self, void arg) {
    return false;
  }

  @override
  bool visitListGames(ListGames self, void arg) {
    return false;
  }

  @override
  bool visitLogin(Login self, void arg) {
    print("Visited Login.");
    return self.uid == "Haui" && self.secret == "ABCDEFG";
  }

  @override
  bool visitReconnect(Reconnect self, void arg) {
    return false;
  }

  @override
  bool visitSelectTrump(SelectTrump self, void arg) {
    return false;
  }

  @override
  bool visitStartGame(StartGame self, void arg) {
    return false;
  }

  @override
  bool visitAddEmail(AddEmail self, void arg) {
    return false;
  }

  @override
  bool visitCreateAccount(CreateAccount self, void arg) {
    return false;
  }

  @override
  bool visitHello(Hello self, void arg) {
    return false;
  }

  @override
  bool visitVerifyEmail(VerifyEmail self, void arg) {
    return false;
  }

}