import 'dart:math';

import 'package:flutter/widgets.dart';

enum _PathCmd {
  none, m, M, L, l, H, h, V, v, c, C, s, S, q, Q, A, a, Z
}

class Coordinate {
  final double x;
  final double y;

  const Coordinate(this.x, this.y);
  const Coordinate.x(this.x) : y = 0;
  const Coordinate.y(this.y) : x = 0;

  operator + (Coordinate other) {
    return Coordinate(x + other.x, y + other.y);
  }

  operator - (Coordinate other) {
    return Coordinate(x - other.x, y - other.y);
  }

  static Coordinate fromString(String spec) {
    var match = RegExp("^([-0-9\\.]+)[,\\s]+([-0-9\\.]+)\$").firstMatch(spec);
    var xSpec = match!.group(1);
    var ySpec = match.group(2);
    return Coordinate(double.parse(xSpec!), double.parse(ySpec!));
  }
}

class Tx {
  final double ox;
  final double oy;
  final double sx;
  final double sy;

  const Tx(this.ox, this.oy, this.sx, this.sy);
  const Tx.translate(double ox, double oy) : this(ox, oy, 1, 1);
  const Tx.scale(double sx, double sy) : this(0, 0, sx, sy);
  static const Tx identity = Tx.translate(0, 0);

  double tx(double x) => ox + sx * x;
  double ty(double y) => oy + sy * y;
}

class _CmdBuffer {
  final List<String> cmds;
  int index = 0;

  _CmdBuffer(this.cmds);

  bool hasNext() => index < cmds.length;
  String peek() => cmds[index];
  String next() => cmds[index++];
  double nextDouble() => double.parse(next());
  int nextInt() => int.parse(next());
  bool nextBool() => nextInt() > 0;
  Coordinate nextCoordinate() => Coordinate(nextDouble(), nextDouble());
  void pop() => index++;
}

abstract class Trace {
  const Trace();
  void applyTo(Tx t, Path path);

  static const Map<String, _PathCmd> cmdByName = {
    "M": _PathCmd.M,
    "m": _PathCmd.m,
    "L": _PathCmd.L,
    "l": _PathCmd.l,
    "H": _PathCmd.H,
    "h": _PathCmd.h,
    "V": _PathCmd.V,
    "v": _PathCmd.v,
    "C": _PathCmd.C,
    "c": _PathCmd.c,
    "S": _PathCmd.S,
    "s": _PathCmd.s,
    "Q": _PathCmd.Q,
    "q": _PathCmd.q,
    "A": _PathCmd.A,
    "a": _PathCmd.a,
    "Z": _PathCmd.Z,
    "z": _PathCmd.Z,
  };

  static Trace fromString(String spec) {
    List<Trace> result = [];
    var values = _CmdBuffer(spec.split(RegExp("[,\\s]+")));
    int index = 0;
    _PathCmd cmd = _PathCmd.none;
    Coordinate last = const Coordinate(0, 0);
    Coordinate before = last;
    while (values.hasNext()) {
      var nextCmd = cmdByName[values.peek()];
      if (nextCmd != null) {
        values.pop();

        switch (nextCmd) {
          case _PathCmd.Z:
            result.add(const ClosePath());
            cmd = _PathCmd.none;
            break;
          default:
            cmd = nextCmd;
        }
        continue;
      }

      // Execute last found command.
      switch (cmd) {
        case _PathCmd.M:
          result.add(MoveTo.coordinate(before = last = values.nextCoordinate()));
          cmd = _PathCmd.L;
          break;
        case _PathCmd.m:
          result.add(MoveTo.coordinate(before = last = last + values.nextCoordinate()));
          cmd = _PathCmd.l;
          break;
        case _PathCmd.L:
          before = last;
          result.add(LineTo.coordinate(last = values.nextCoordinate()));
          break;
        case _PathCmd.l:
          before = last;
          result.add(LineTo.coordinate(last += values.nextCoordinate()));
          break;
        case _PathCmd.H:
          before = last;
          last = Coordinate(values.nextDouble(), last.y);
          result.add(LineTo.coordinate(last));
          break;
        case _PathCmd.h:
          before = last;
          last = Coordinate(last.x + values.nextDouble(), last.y);
          result.add(LineTo.coordinate(last));
          break;
        case _PathCmd.V:
          before = last;
          last = Coordinate(last.x, values.nextDouble());
          result.add(LineTo.coordinate(last));
          break;
        case _PathCmd.v:
          before = last;
          last = Coordinate(last.x, last.y + values.nextDouble());
          result.add(LineTo.coordinate(last));
          break;
        case _PathCmd.C:
          result.add(CubicTo.coordinate(
            values.nextCoordinate(),
            before = values.nextCoordinate(),
            last = values.nextCoordinate(),
          ));
          break;
        case _PathCmd.c:
          result.add(CubicTo.coordinate(
            last + values.nextCoordinate(),
            before = last + values.nextCoordinate(),
            last += values.nextCoordinate()));
          break;
        case _PathCmd.S:
          var p1 = last + (last - before);
          var p2 = values.nextCoordinate();
          result.add(CubicTo.coordinate(
            p1,
            before = p2,
            last = values.nextCoordinate(),
          ));
          break;
        case _PathCmd.s:
          var p1 = last + (last - before);
          var p2 = last + values.nextCoordinate();
          result.add(CubicTo.coordinate(
            p1,
            before = p2,
            last += values.nextCoordinate(),
          ));
          break;
        case _PathCmd.Q:
          var p1 = values.nextCoordinate();
          result.add(CubicTo.coordinate(
            p1,
            before = p1,
            last = values.nextCoordinate(),
          ));
          break;
        case _PathCmd.q:
          var p1 = last + values.nextCoordinate();
          result.add(CubicTo.coordinate(
              p1,
              before = p1,
              last += values.nextCoordinate()));
          break;
        case _PathCmd.A:
          result.add(
              ArcTo.coordinate(
                  values.nextCoordinate(),
                  values.nextDouble(),
                  values.nextBool(),
                  values.nextBool(),
                  before = last = values.nextCoordinate()));
          break;
        case _PathCmd.a:
          result.add(
              ArcTo.coordinate(
                  values.nextCoordinate(),
                  values.nextDouble(),
                  values.nextBool(),
                  values.nextBool(),
                  before = last += values.nextCoordinate()));
          break;
        case _PathCmd.none:
          assert(false, "Expecting path command, found: " + values.peek());
      }
    }

    return result.length == 1 ? result[0] : Traces(result);
  }
}

class Traces extends Trace {
  final List<Trace> parts;

  const Traces(this.parts);

  @override
  void applyTo(Tx t, Path path) {
    for (var p in parts) {
      p.applyTo(t, path);
    }
  }
}

class MoveTo extends Trace {
  final double dx;
  final double dy;

  const MoveTo(this.dx, this.dy);
  MoveTo.coordinate(Coordinate p1) : dx = p1.x, dy = p1.y;

  @override
  void applyTo(Tx t, Path path) {
    path.moveTo(t.tx(dx), t.ty(dy));
  }
}

class LineTo extends Trace {
  final double dx;
  final double dy;

  const LineTo(this.dx, this.dy);
  LineTo.coordinate(Coordinate p1) : dx = p1.x, dy = p1.y;

  @override
  void applyTo(Tx t, Path path) {
    path.lineTo(t.tx(dx), t.ty(dy));
  }
}

class CubicTo extends Trace {
  final double x1;
  final double y1;
  final double x2;
  final double y2;
  final double x3;
  final double y3;

  const CubicTo(this.x1, this.y1, this.x2, this.y2, this.x3, this.y3);

  CubicTo.coordinate(Coordinate p1, Coordinate p2, Coordinate p3) :
    x1 = p1.x,
    y1 = p1.y,
    x2 = p2.x,
    y2 = p2.y,
    x3 = p3.x,
    y3 = p3.y;

  @override
  void applyTo(Tx t, Path path) {
    path.cubicTo(t.tx(x1), t.ty(y1), t.tx(x2), t.ty(y2), t.tx(x3), t.ty(y3));
  }

}

class ArcTo extends Trace {
  final double x;
  final double y;
  final double rx;
  final double ry;
  final bool large;
  final bool clockwise;
  final double rotation;

  ArcTo.coordinate(Coordinate r, this.rotation, this.large, this.clockwise, Coordinate p) :
        x = p.x,
        y = p.y,
        rx = r.x,
        ry = r.y;

  @override
  void applyTo(Tx t, Path path) {
    path.arcToPoint(
        Offset(t.tx(x), t.ty(y)),
        radius: Radius.elliptical(rx, ry),
        rotation: rotation,
        largeArc: large,
        clockwise: clockwise,
    );
  }

}

class ClosePath extends Trace {
  const ClosePath();

  @override
  void applyTo(Tx t, Path path) {
    path.close();
  }
}

Paint colorPaint(Color color) =>
    Paint()
      ..color = color
      ..isAntiAlias = true;

class SVGPathPainter extends CustomPainter {

  final Paint _paint;
  final Trace _spec;

  const SVGPathPainter(this._paint, this._spec);

  @override
  void paint(Canvas canvas, Size size) {
    var boxWidth = 50;
    var boxHeight = 50;

    var sx = size.width / boxWidth;
    var sy = size.height / boxHeight;
    var f = min(sx, sy);

    var w = boxWidth * f;
    var h = boxHeight * f;

    var ox = (size.width - w) / 2;
    var oy = (size.height - h) / 2;

    Path p = Path();
    _spec.applyTo(Tx(ox, oy, f, f), p);
    canvas.drawPath(p, _paint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) {
    return true;
  }
}
