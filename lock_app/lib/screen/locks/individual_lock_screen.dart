import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_platform_widgets/flutter_platform_widgets.dart';
import 'package:lock_app/bloc/authentication/authentication_bloc.dart';
import 'package:lock_app/bloc/authentication/authentication_state.dart';
import 'package:lock_app/data/lock/lock.dart';
import 'package:lock_app/repository/lock_repository/http_lock_repository.dart';
import 'package:lock_app/repository/user_repository/http_user_repository.dart';
import 'package:lock_app/widget/open_page.dart';

import 'invite_users_screen.dart';

class IndividualLockScreen extends StatefulWidget {
  final Lock data;

  IndividualLockScreen(this.data);

  @override
  _IndividualLockScreenState createState() => _IndividualLockScreenState();
}

class _IndividualLockScreenState extends State<IndividualLockScreen> {
//  int userId;

  @override
  Widget build(BuildContext context) {
    var textStyleOpen = TextStyle(fontSize: 30.0, color: Colors.green);
    var textStyleClose = TextStyle(fontSize: 30.0, color: Colors.red);
    return PlatformScaffold(
      appBar: PlatformAppBar(
        title: Text('${widget.data.name}'),
      ),
      body: Stack(
        //falta mostrar los usuarios
        children: <Widget>[
          ListView(
            children: <Widget>[
              SizedBox(height: 15.0),
//              widget.data.userAdminId == userId
//                  ? Column(
//                      children: <Widget>[
//                        Center(
//                          child: PlatformButton(
//                            padding: EdgeInsets.symmetric(
//                                horizontal: 30.0, vertical: 10.0),
//                            ios: (_) => CupertinoButtonData(
//                                borderRadius: BorderRadius.circular(15.0)),
//                            color: Colors.green,
//                            onPressed: () => openPage(
//                                (_) => InviteUsersScreen(widget.data), context),
//                            android: (_) => MaterialRaisedButtonData(
//                                padding: EdgeInsets.symmetric(
//                                    vertical: 12.0, horizontal: 30.0),
//                                shape: StadiumBorder(),
//                                textColor: Colors.white),
//                            child: PlatformText(
//                              'Invite users',
//                              style: TextStyle(fontSize: 16.0),
//                            ),
//                          ),
//                        ),
//                        SizedBox(
//                          height: 15.0,
//                        )
//                      ],
//                    )
//                  : Container(),
              widget.data.opened
                  ? Center(
                      child: Text(
                        'Lock is now opened',
                        style: textStyleOpen,
                      ),
                    )
                  : Center(
                      child: Text(
                        'Lock is now closed',
                        style: textStyleClose,
                      ),
                    ),
              SizedBox(height: 15.0),
//              AnimatedContainer(
//                color: Colors.blue,
//                height: 50,
//                width: widget.data.opened ? 100 : 50,
//                duration: Duration(seconds: 1),
//              ),
//              SizedBox(height: 15.0),

              Center(
                child: PlatformButton(
                  padding:
                      EdgeInsets.symmetric(horizontal: 30.0, vertical: 10.0),
                  ios: (_) => CupertinoButtonData(
                      borderRadius: BorderRadius.circular(15.0)),
                  color: Colors.greenAccent,
                  onPressed: () {
                    HttpLockRepository.openLock(widget.data.uuid, context);
                    setState(() {
                      widget.data.opened = true;
                      print(widget.data.opened);
                    });
                  },
                  android: (_) => MaterialRaisedButtonData(
                      padding: EdgeInsets.symmetric(
                          vertical: 12.0, horizontal: 30.0),
                      shape: StadiumBorder(),
                      textColor: Colors.white),
                  child: PlatformText(
                    'Open lock',
                    style: TextStyle(fontSize: 16.0),
                  ),
                ),
              ),
              SizedBox(height: 15.0),
              Center(
                child: PlatformButton(
                  padding:
                      EdgeInsets.symmetric(horizontal: 30.0, vertical: 10.0),
                  ios: (_) => CupertinoButtonData(
                      borderRadius: BorderRadius.circular(15.0)),
                  color: Colors.redAccent,
                  onPressed: () {
                    HttpLockRepository.closeLock(widget.data.uuid, context);
                    setState(() {
                      widget.data.opened = false;
                      print(widget.data.opened);
                    });
                  },
                  android: (_) => MaterialRaisedButtonData(
                      padding: EdgeInsets.symmetric(
                          vertical: 12.0, horizontal: 30.0),
                      shape: StadiumBorder(),
                      textColor: Colors.white),
                  child: PlatformText(
                    'Close lock',
                    style: TextStyle(fontSize: 16.0),
                  ),
                ),
              ),
            ],
          )
        ],
      ),
    );
  }

//  void getUserId() async {
//    userId = await HttpUserRepository.getUser(context);
//  }

  @override
  void initState() {
    super.initState();
  }
}
