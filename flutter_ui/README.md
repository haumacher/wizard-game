# Wizard client written in Flutter/Dart

The Wizard mobile edition. Allows to connect to the same Java server as the native JavaFX client. From the Flutter
source, a web version, an Android app and an iOS app can be built. 

The [library for cross-platform communication](lib/msg.dart) is generated from a single `msgbuf` 
[protocol definition](../common/src/main/java/de/haumacher/wizard/msg/msg.proto).