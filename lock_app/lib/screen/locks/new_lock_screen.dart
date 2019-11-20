import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_platform_widgets/flutter_platform_widgets.dart';
import 'package:lock_app/bloc/lock/lock_bloc.dart';
import 'package:lock_app/data/lock/lock.dart';
import 'package:lock_app/data/user/user.dart';
import 'package:flutter_multiselect/flutter_multiselect.dart';
import 'package:lock_app/repository/lock_repository/http_lock_repository.dart';
import 'package:lock_app/widget/open_page.dart';

import 'invite_users_screen.dart';

class NewLockScreen extends StatefulWidget {
  @override
  _NewLockScreenState createState() => _NewLockScreenState();
}

class _NewLockScreenState extends State<NewLockScreen> {
  TextEditingController _lockNameController;
  String _lockNameErrorText;

  TextEditingController _uuidController;
  String _uuidErrorText;

  @override
  Widget build(BuildContext context) {
    return PlatformScaffold(
      appBar: PlatformAppBar(
        ios: (_) => CupertinoNavigationBarData(transitionBetweenRoutes: false),
        title: Text('Add new lock'),
        backgroundColor: Colors.green,
      ),
      body: ListView(
        children: <Widget>[
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 24.0),
            child: Column(
              children: <Widget>[
                SizedBox(height: 15.0),
                PlatformTextField(
                  controller: _lockNameController,
                  ios: (_) => CupertinoTextFieldData(
                      padding: EdgeInsets.symmetric(vertical: 15.0),
                      placeholder: 'Lock name',
                      decoration:
                          BoxDecoration(border: Border(bottom: BorderSide()))),
                  android: (_) => MaterialTextFieldData(
                      decoration: InputDecoration(
                          errorText: _lockNameErrorText,
                          labelText: 'Lock name')),
                ),
                SizedBox(height: 15.0),
                PlatformTextField(
                  controller: _uuidController,
                  ios: (_) => CupertinoTextFieldData(
                      padding: EdgeInsets.symmetric(vertical: 15.0),
                      placeholder: 'Lock uuid',
                      decoration:
                          BoxDecoration(border: Border(bottom: BorderSide()))),
                  android: (_) => MaterialTextFieldData(
                      decoration: InputDecoration(
                          errorText: _lockNameErrorText,
                          labelText: 'Lock uuid')),
                ),
                SizedBox(
                  height: 15.0,
                ),
                Center(
                  child: PlatformButton(
                    padding:
                        EdgeInsets.symmetric(horizontal: 30.0, vertical: 10.0),
                    ios: (_) => CupertinoButtonData(
                        borderRadius: BorderRadius.circular(15.0)),
                    color: Colors.green,
                    onPressed: () async {
                      submit();
                    },
                    android: (_) => MaterialRaisedButtonData(
                        padding: EdgeInsets.symmetric(
                            vertical: 12.0, horizontal: 30.0),
                        shape: StadiumBorder(),
                        textColor: Colors.white),
                    child: PlatformText(
                      'Add new lock',
                      style: TextStyle(fontSize: 16.0),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  void submit() async {
    Map body = {'name': _lockNameController.text, 'uuid': _uuidController.text};

    BlocProvider.of<LockBloc>(context).onNewLockTapped(context, body);
//    Lock lastLock = BlocProvider.of<LockBloc>(context).getLastLock;
//    openPage((_) => InviteUsersScreen(lastLock), context);
    Navigator.pop(context);
  }

  @override
  void initState() {
    super.initState();
    _lockNameController = TextEditingController();
    _uuidController =
        TextEditingController(text: '18bfd86f-539e-40e2-a917-64c9ed1d42d9');
  }

  @override
  void dispose() {
    _lockNameController.dispose();
    _uuidController.dispose();
    super.dispose();
  }
}
