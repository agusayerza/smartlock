import 'package:flutter/material.dart';
import 'package:flutter_platform_widgets/flutter_platform_widgets.dart';

class LoadingWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Center(
        child: PlatformCircularProgressIndicator(
      ios: (_) => CupertinoProgressIndicatorData(radius: 15.0),
    ));
  }
}
