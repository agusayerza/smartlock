import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_platform_widgets/flutter_platform_widgets.dart';
import 'package:lock_app/bloc/authentication/authentication_bloc.dart';
import 'package:lock_app/screen/locks/visualize_locks_screen.dart';
import 'package:lock_app/widget/open_page.dart';

import 'new_lock_screen.dart';

class LocksScreen extends StatefulWidget {
  @override
  _LocksScreenState createState() => _LocksScreenState();
}

class _LocksScreenState extends State<LocksScreen> {
  @override
  Widget build(BuildContext context) {
    return PlatformScaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        mainAxisSize: MainAxisSize.max,
        children: <Widget>[
          SizedBox(height: 15.0),
          Center(
            child: PlatformButton(
              padding: EdgeInsets.symmetric(horizontal: 30.0, vertical: 10.0),
              ios: (_) => CupertinoButtonData(
                  borderRadius: BorderRadius.circular(15.0)),
              color: Colors.red,
              onPressed: () => openPage((_) => NewLockScreen(), context),
              android: (_) => MaterialRaisedButtonData(
                  padding:
                      EdgeInsets.symmetric(vertical: 12.0, horizontal: 30.0),
                  shape: StadiumBorder(),
                  textColor: Colors.white),
              child: PlatformText(
                'Add new lock',
                style: TextStyle(fontSize: 16.0),
              ),
            ),
          ),
          SizedBox(height: 15.0),
          Center(
            child: PlatformButton(
              padding: EdgeInsets.symmetric(horizontal: 30.0, vertical: 10.0),
              ios: (_) => CupertinoButtonData(
                  borderRadius: BorderRadius.circular(15.0)),
              color: Colors.red,
              onPressed: () => openPage((_) => MyLocksScreen(), context),
              android: (_) => MaterialRaisedButtonData(
                  padding:
                      EdgeInsets.symmetric(vertical: 12.0, horizontal: 30.0),
                  shape: StadiumBorder(),
                  textColor: Colors.white),
              child: PlatformText(
                'View my locks',
                style: TextStyle(fontSize: 16.0),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
