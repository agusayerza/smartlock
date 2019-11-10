import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_platform_widgets/flutter_platform_widgets.dart';
import 'package:lock_app/screen/locks/locks_screen.dart';
import 'package:lock_app/screen/profile/profile_screen.dart';
import 'package:lock_app/widget/open_page.dart';

class Home extends StatefulWidget {
  Home({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {
  int _selectedTabIndex = 0;

  //Distintas screens
  List<Widget> _children = [
    Scaffold(body: Container()),
    Scaffold(body: ProfileScreen()),
    Scaffold(body: LocksScreen()),
  ];

  @override
  Widget build(BuildContext context) {
    return PlatformScaffold(
        appBar: PlatformAppBar(
//          trailingActions: <Widget>[
//            IconButton(
//              icon: Icon(Icons.search),
////              onPressed: () => openPage((_) => SearchScreen(), context),
//            )
//          ],
          ios: (_) =>
              CupertinoNavigationBarData(transitionBetweenRoutes: false),
          title: Text('SmartLock'),
        ),
        backgroundColor: Colors.white,
        body: _children[_selectedTabIndex], // new
        bottomNavBar: PlatformNavBar(
          // PlatformNavBar(
          android: (_) =>
              MaterialNavBarData(type: BottomNavigationBarType.fixed),
          currentIndex: _selectedTabIndex,
          itemChanged: (index) => setState(
            () => _selectedTabIndex = index,
          ),
          items: [
            BottomNavigationBarItem(
              icon: Icon(Icons.home),
              title: Text('Home'),
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.person),
              title: Text('Profile'),
            ),
            BottomNavigationBarItem(
              icon: Icon(CupertinoIcons.car),
              title: Text('Locks'),
            ),
          ],
        ));
  }
}
