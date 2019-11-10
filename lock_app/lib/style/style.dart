import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

ThemeData materialAppTheme() {
  return ThemeData(
      primaryColor: Colors.green,
      accentColor: Colors.greenAccent,
      sliderTheme: SliderThemeData(
          inactiveTickMarkColor: Colors.green,
          disabledActiveTickMarkColor: Colors.green,
          disabledActiveTrackColor: Colors.green,
          showValueIndicator: ShowValueIndicator.always,
          thumbColor: Colors.green,
          activeTickMarkColor: Colors.green,
          valueIndicatorColor: Colors.green,
          activeTrackColor: Colors.green));
}

CupertinoThemeData cupertinoTheme() {
  return CupertinoThemeData(primaryColor: Colors.green,barBackgroundColor: Colors.green);
}
