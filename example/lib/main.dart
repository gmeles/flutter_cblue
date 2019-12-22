import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_cblue/flutter_cblue.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await FlutterCblue.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  String greekString = "^xa ^LL150 " +
      "^ci28" +
      "^cwN,e:TT0003M_.FNT" +
      "^xz " +
      "^xa" +
      "^lh0,0" +
      "^fo10,30^aN,20,20^fdΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ^fs" +
      "^fo10,60^aN,20,20^fdαβγδεζηθικλμνξοπρστυφχψω^fs" +
      "^fo10,90^aN,20,20^fdΆΈΉΊΌΥΏάέήίόύώ^fs" +
      "^pq1" +
      "^xz";


  void printBT(String _str) async {
    print(await FlutterCblue.printToBT(printStr: _str)); // default logType
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
            child: FlatButton(
          child: Text('Running on: $_platformVersion\n'),
          onPressed: () {
            printBT(greekString);
          },
        )),
      ),
    );
  }
}
