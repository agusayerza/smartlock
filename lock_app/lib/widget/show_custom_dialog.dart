import 'package:flutter/material.dart';
import 'package:flutter_platform_widgets/flutter_platform_widgets.dart';

void showCustomDialog(BuildContext context, String title, String body) {
  showPlatformDialog(
    context: context,
    builder: (_) => PlatformAlertDialog(
      title: Text(title),
      content: Text(body),
      actions: <Widget>[
        PlatformDialogAction(
          child: PlatformText('OK'),
          onPressed: () => Navigator.pop(context),
        ),
      ],
    ),
  );
}
