import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_platform_widgets/flutter_platform_widgets.dart';
import 'package:lock_app/bloc/authentication/authentication_bloc.dart';

Future<bool> showOnPopDialog(BuildContext context) {
  return showPlatformDialog(
    context: context,
    builder: (_) => PlatformAlertDialog(
      title: Text('Warning'),
      content: Text(
          '¿Esta seguro que quiere irse de esta página?, todo su progreso se perderá'),
      actions: <Widget>[
        PlatformDialogAction(
            child: PlatformText('Quiero irme'),
            onPressed: () {
              BlocProvider.of<AuthenticationBloc>(context).reset();
              Navigator.pop(context, true);
            }),
        PlatformDialogAction(
          child: PlatformText('Quiero quedarme'),
          onPressed: () => Navigator.pop(context, false),
        ),
      ],
    ),
  );
}
