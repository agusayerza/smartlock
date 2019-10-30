import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

ThemeData materialAppTheme() {
  return ThemeData(
      primaryColor: Colors.red,
      accentColor: Colors.deepOrangeAccent,
      sliderTheme: SliderThemeData(
          inactiveTickMarkColor: Colors.red,
          disabledActiveTickMarkColor: Colors.red,
          disabledActiveTrackColor: Colors.red,
          showValueIndicator: ShowValueIndicator.always,
          thumbColor: Colors.red,
          activeTickMarkColor: Colors.red,
          valueIndicatorColor: Colors.red,
          activeTrackColor: Colors.red));
}

CupertinoThemeData cupertinoTheme() {
  return CupertinoThemeData(primaryColor: Colors.red,barBackgroundColor: Colors.red);
}
