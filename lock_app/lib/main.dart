import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_platform_widgets/flutter_platform_widgets.dart';
import 'package:lock_app/repository/user_repository/http_user_repository.dart';
import 'package:lock_app/screen/home/home.dart';
import 'package:lock_app/screen/login/login_screen.dart';
import 'package:lock_app/screen/register/register_screen.dart';
import 'package:lock_app/style/style.dart';
import 'package:lock_app/widget/loading_widget.dart';
import 'package:lock_app/widget/provide_all.dart';

import 'bloc/authentication/authentication_bloc.dart';
import 'bloc/authentication/authentication_state.dart';
import 'bloc/lock/lock_bloc.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    AuthenticationBloc bloc = AuthenticationBloc();
    var http = HttpUserRepository(bloc);
    bloc.userRepository = http;

    return provideAll(
      builders: [
//        (c) => BlocProvider<SignUpBloc>(builder: (c) => SignUpBloc(), child: c),
        (c) => BlocProvider<AuthenticationBloc>(builder: (c) => bloc, child: c),
        (c) => BlocProvider<LockBloc>(
              builder: (c) => LockBloc(),
              child: c,
            )
      ],
      child: PlatformProvider(
          builder: (BuildContext context) => PlatformApp(
                routes: {'home': (_) => Home()},
                title: 'The Smart Lock App',
                android: (_) => MaterialAppData(theme: materialAppTheme()),
                ios: (_) => CupertinoAppData(theme: cupertinoTheme()),
                home: App(),
              )),
    );
  }
}

class App extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return PlatformScaffold(
      body: BlocListener(
        bloc: BlocProvider.of<AuthenticationBloc>(context),
        child: BlocBuilder(
          bloc: BlocProvider.of<AuthenticationBloc>(context),
          builder: (BuildContext context, state) {
            if (state is InitialRegistrationState) {
              return RegisterScreen();
            } else if (state is InitialAuthenticationState) {
              return LoginScreen();
            } else if (state is AuthLoadingState) {
              return LoadingWidget();
            } else if (state is AuthenticatedState) {
              // pass token
              return Home();
            } else if (state is AuthenticationError) {
              return RegisterScreen();
            } else if (state is LogOutErrorState) {
              return Home();
            } else {
              return RegisterScreen();
            }
          },
        ),
        listener: (BuildContext context, state) {},
//        listener: (BuildContext context, state) {
//          if (state is AuthenticationError) {
//            showCustomDialog(context, 'Error', state.message);
//          }
//          if (state is DeleteSuccessState) {
//            showCustomDialog(context, 'Exito', state.message);
//          }
//          if (state is DeleteErrorState) {
//            showCustomDialog(context, 'Error', state.message);
//          }
//          if (state is LogOutErrorState) {
//            showCustomDialog(context, 'Error', state.message);
//          }
//        },
      ),
    );
  }
}
