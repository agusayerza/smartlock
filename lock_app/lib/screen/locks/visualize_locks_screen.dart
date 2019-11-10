import 'package:flutter_platform_widgets/flutter_platform_widgets.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:lock_app/bloc/lock/lock_bloc.dart';
import 'package:lock_app/bloc/lock/lock_bloc_state.dart';
import 'package:lock_app/widget/open_page.dart';

import 'individual_lock_screen.dart';
import 'new_lock_screen.dart';

class MyLocksScreen extends StatefulWidget {
//  final HttpReservationRepository reservation = HttpReservationRepository();

  @override
  _MyLocksScreenState createState() => _MyLocksScreenState();
}

class _MyLocksScreenState extends State<MyLocksScreen> {
  @override
  Widget build(BuildContext context) {
    return PlatformScaffold(
      appBar: PlatformAppBar(
        title: Text('My locks'),
      ),
      body: BlocBuilder(
        bloc: BlocProvider.of<LockBloc>(context),
        builder: (BuildContext context, state) {
          if (state is ListLockState) {
            return state.locks.length == 0
                ? Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      Center(child: Text('You have no locks ')),
                      SizedBox(height: 10.0),
                      PlatformButton(
                        android: (_) =>
                            MaterialRaisedButtonData(shape: StadiumBorder()),
                        color: Colors.red,
                        onPressed: () =>
                            openPage((_) => NewLockScreen(), context),
                        child: Text(
                          'Add new lock',
                          style: TextStyle(color: Colors.white),
                        ),
                      )
                    ],
                  )
                : Stack(
                    children: <Widget>[
                      ListView(
                        children: state.locks
                            .map((data) => Dismissible(
                                  key: Key(UniqueKey().toString()),
                                  confirmDismiss: (_) async {
                                    bool result = await showDialog(
                                        context: context,
                                        builder: (_) => AlertDialog(
                                              title: Text('Warning'),
                                              content: Text(
                                                  'Are you sure you want to delete this lock?'),
                                              actions: <Widget>[
                                                FlatButton(
                                                  child: Text('Delete'),
                                                  onPressed: () {
                                                    BlocProvider.of<LockBloc>(
                                                            context)
                                                        .deleteLockFromList(
                                                            context, data);
                                                    Navigator.pop(
                                                        context, true);
                                                    return true;
                                                  },
                                                ),
                                                FlatButton(
                                                  child: Text('Cancel'),
                                                  onPressed: () {
                                                    Navigator.pop(
                                                        context, false);
                                                    return false;
                                                  },
                                                )
                                              ],
                                            ));
                                    return result;
                                  },
                                  direction: DismissDirection.endToStart,
                                  background: Container(
                                    alignment: Alignment.centerRight,
                                    padding: const EdgeInsets.only(right: 40.0),
                                    color: Colors.red,
                                    child: Icon(
                                      Icons.delete,
                                      color: Colors.white,
                                    ),
                                  ),
                                  child: Container(
                                    padding: const EdgeInsets.symmetric(
                                        horizontal: 20.0, vertical: 10.0),
                                    child: InkWell(
                                      onTap: () {
                                        openPage(
                                            (_) => IndividualLockScreen(
                                                  data,
                                                ),
                                            context);
                                      },
                                      child: Row(
                                        children: <Widget>[
                                          Flexible(
                                            fit: FlexFit.tight,
                                            flex: 3,
                                            child: Container(
                                              height: 150,
                                              child: Icon(
                                                Icons.lock,
                                                size: 75,
                                              ),
                                            ),
                                          ),
                                          Flexible(
                                            flex: 4,
                                            fit: FlexFit.loose,
                                            child: Padding(
                                              padding: const EdgeInsets.only(
                                                  left: 30.0),
                                              child: Column(
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.start,
                                                children: <Widget>[
                                                  Text(
                                                    'Name: ${data.name}',
                                                    style:
                                                        TextStyle(fontSize: 21),
                                                  ),
                                                ],
                                              ),
                                            ),
                                          )
                                        ],
                                      ),
                                    ),
                                  ),
                                ))
                            .toList(),
                      ),
                      Positioned(
                        bottom: 40.0,
                        right: 0.0,
                        child: RaisedButton(
                          padding:
                              const EdgeInsets.fromLTRB(40.0, 16.0, 30.0, 16.0),
                          color: Colors.red,
                          elevation: 0,
                          shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.only(
                                  topLeft: Radius.circular(30.0),
                                  bottomLeft: Radius.circular(30.0))),
                          onPressed: () async {
                            openPage((_) => NewLockScreen(), context);
                          },
                          child: Row(
                            mainAxisSize: MainAxisSize.min,
                            children: <Widget>[
                              Text(
                                "Add new lock".toUpperCase(),
                                style: TextStyle(
                                    fontWeight: FontWeight.bold,
                                    fontSize: 16.0,
                                    color: Colors.white),
                              ),
                              SizedBox(width: 30.0),
                            ],
                          ),
                        ),
                      )
                    ],
                  );
          } else {
            return Container(
              color: Colors.green,
            );
          }
        },
      ),
    );
  }

  @override
  void initState() {
    super.initState();
    BlocProvider.of<LockBloc>(context).loadLockList(context);
  }
}
