import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_platform_widgets/flutter_platform_widgets.dart';
import 'package:lock_app/data/lock/lock.dart';
import 'package:lock_app/repository/lock_repository/http_lock_repository.dart';
import 'package:lock_app/screen/locks/set_schedule_screen.dart';
import 'package:lock_app/widget/open_page.dart';
import 'package:lock_app/widget/show_custom_dialog.dart';

class InviteUsersScreen extends StatefulWidget {
  final Lock lock;
  InviteUsersScreen(this.lock);

  @override
  _InviteUsersScreenState createState() => _InviteUsersScreenState();
}

class _InviteUsersScreenState extends State<InviteUsersScreen> {
  TextEditingController _invitedUsersController;
  String _invitedUsersErrorText;

  @override
  Widget build(BuildContext context) {
    return PlatformScaffold(
      appBar: PlatformAppBar(
        ios: (_) => CupertinoNavigationBarData(transitionBetweenRoutes: false),
        title: Text('Invite users to this lock'),
        backgroundColor: Colors.red,
      ),
      body: ListView(
        children: <Widget>[
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 24.0),
            child: Column(
              children: <Widget>[
                SizedBox(height: 15.0),
                PlatformTextField(
                  controller: _invitedUsersController,
                  ios: (_) => CupertinoTextFieldData(
                      padding: EdgeInsets.symmetric(vertical: 15.0),
                      placeholder: 'Lock name',
                      decoration:
                          BoxDecoration(border: Border(bottom: BorderSide()))),
                  android: (_) => MaterialTextFieldData(
                      decoration: InputDecoration(
                          errorText: _invitedUsersErrorText,
                          labelText: 'Invited users')),
                ),
                SizedBox(height: 15.0),
                Center(
                  child: PlatformButton(
                    padding:
                        EdgeInsets.symmetric(horizontal: 30.0, vertical: 10.0),
                    ios: (_) => CupertinoButtonData(
                        borderRadius: BorderRadius.circular(15.0)),
                    color: Colors.red,
                    onPressed: () async {
                      submit();
                    },
                    android: (_) => MaterialRaisedButtonData(
                        padding: EdgeInsets.symmetric(
                            vertical: 12.0, horizontal: 30.0),
                        shape: StadiumBorder(),
                        textColor: Colors.white),
                    child: PlatformText(
                      'Invite',
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
                    color: Colors.red,
                    onPressed: () =>
                        openPage((_) => SetScheduleScreen(), context),
                    android: (_) => MaterialRaisedButtonData(
                        padding: EdgeInsets.symmetric(
                            vertical: 12.0, horizontal: 30.0),
                        shape: StadiumBorder(),
                        textColor: Colors.white),
                    child: PlatformText(
                      'Setup user permissions',
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
    Map body = {
      'email': _invitedUsersController.value.toString(),
      'lockId': widget.lock.id,
    };
    try {
      await HttpLockRepository.addUserToThisLock(context, body);
      showCustomDialog(
          context,
          'Success',
          _invitedUsersController.value.toString() +
              ' was invited successfully');
      _invitedUsersController.text = '';
    } catch (e) {
      showCustomDialog(context, 'Error',
          'Could not invite ' + _invitedUsersController.value.toString());
    }
  }

  @override
  void initState() {
    super.initState();
  }
}
