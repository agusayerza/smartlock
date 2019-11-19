import 'package:flutter/material.dart';
import 'package:flutter_platform_widgets/flutter_platform_widgets.dart';

openPage(WidgetBuilder pageToDisplayBuilder, context) {
  Navigator.push(
      context,
      platformPageRoute(
        builder: pageToDisplayBuilder,
      ));
}
