import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_platform_widgets/flutter_platform_widgets.dart';
import 'package:lock_app/bloc/authentication/authentication_bloc.dart';
import 'package:lock_app/widget/open_page.dart';

class ProfileScreen extends StatefulWidget {
  @override
  _ProfileScreenState createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
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
              onPressed: () async {
                bool result = await showDialog(
                    context: context,
                    builder: (_) => AlertDialog(
                            title: Text('Alerta'),
                            content: Text(
                                'Are you sure you want to delete your account?'),
                            actions: <Widget>[
                              FlatButton(
                                child: Text('Delete'),
                                onPressed: () {
                                  Navigator.pop(context, true);
                                  return true;
                                },
                              ),
                              FlatButton(
                                child: Text('Cancel'),
                                onPressed: () {
                                  Navigator.pop(context, false);
                                  return false;
                                },
                              )
                            ]));
                if (result != null && result) {
                  BlocProvider.of<AuthenticationBloc>(context)
                      .deleteUser(context);
                }
              },
              android: (_) => MaterialRaisedButtonData(
                  padding:
                      EdgeInsets.symmetric(vertical: 12.0, horizontal: 30.0),
                  shape: StadiumBorder(),
                  textColor: Colors.white),
              child: PlatformText(
                'Delete user',
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
              onPressed: () => BlocProvider.of<AuthenticationBloc>(context)
                  .onLogOutButtonTapped(context),
              android: (_) => MaterialRaisedButtonData(
                  padding:
                      EdgeInsets.symmetric(vertical: 12.0, horizontal: 30.0),
                  shape: StadiumBorder(),
                  textColor: Colors.white),
              child: PlatformText(
                'Log out',
                style: TextStyle(fontSize: 16.0),
              ),
            ),
          ),
        ],
      ),
    );
  }

  @override
  void initState() {
    super.initState();
  }
}
