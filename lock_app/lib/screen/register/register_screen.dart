import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_platform_widgets/flutter_platform_widgets.dart';
import 'package:lock_app/bloc/authentication/authentication_bloc.dart';
import 'package:lock_app/bloc/authentication/authentication_state.dart';
import 'package:lock_app/data/user/user_sign_up_form.dart';
import 'package:lock_app/widget/show_custom_dialog.dart';
import 'package:lock_app/widget/show_pop_dialog.dart';

class RegisterScreen extends StatefulWidget {
  @override
  _RegisterScreenState createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final _emailController = TextEditingController();
  String _emailErrorText;

  final _passwordController = TextEditingController();
  String _passwordErrorText;

  final _confirmPasswordController = TextEditingController();
  String _confirmPasswordErrorText;

  bool _isLoading = false;

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      child: PlatformScaffold(
          backgroundColor: Colors.white,
          body: BlocListener(
            listener: (BuildContext context, state) {
              if (state is AuthenticatedState) {
                Navigator.pop(context);
                BlocProvider.of<AuthenticationBloc>(context).reset();
              } else if (state is AuthenticationError) {
                setState(() => _isLoading = false);
//              showCustomDialog(context, 'Error', state.message);
              } else if (state is InitialAuthenticationState) {
                setState(() => _isLoading = false);
//                BlocProvider.of<AuthenticationBloc>(context).reset();
//                Navigator.pop(context);
              }
            },
            bloc: BlocProvider.of<AuthenticationBloc>(context),
            child: Stack(
              children: <Widget>[
                ListView(
                  children: <Widget>[
                    buildAppBar(context),
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 24.0),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: <Widget>[
                          SizedBox(height: 5.0),
                          PlatformText(
                            'Register new account',
                            style: TextStyle(
                                fontSize: 24.0, fontWeight: FontWeight.bold),
                          ),
                          SizedBox(height: 15.0),
                          PlatformTextField(
                            controller: _emailController,
                            ios: (_) => CupertinoTextFieldData(
                                suffix: _emailErrorText == null
                                    ? Container()
                                    : Icon(Icons.error),
                                padding: EdgeInsets.symmetric(vertical: 15.0),
                                placeholder: 'Email',
                                decoration: BoxDecoration(
                                    border:
                                        Border(bottom: BorderSide(width: .5)))),
                            android: (_) => MaterialTextFieldData(
                                decoration: InputDecoration(
                                    errorText: _emailErrorText,
                                    labelText: 'email')),
                          ),
                          SizedBox(height: 15.0),
                          PlatformTextField(
                            obscureText: true,
                            controller: _passwordController,
                            ios: (_) => CupertinoTextFieldData(
                                suffix: _passwordErrorText == null
                                    ? Container()
                                    : Icon(Icons.error),
                                padding: EdgeInsets.symmetric(vertical: 15.0),
                                placeholder: 'Password',
                                decoration: BoxDecoration(
                                    border:
                                        Border(bottom: BorderSide(width: .5)))),
                            android: (_) => MaterialTextFieldData(
                                decoration: InputDecoration(
                                    errorText: _passwordErrorText,
                                    labelText: 'Password')),
                          ),
                          SizedBox(height: 15.0),
                          PlatformTextField(
                            obscureText: true,
                            controller: _confirmPasswordController,
                            ios: (_) => CupertinoTextFieldData(
                                suffix: _confirmPasswordErrorText == null
                                    ? Container()
                                    : Icon(Icons.error),
                                padding: EdgeInsets.symmetric(vertical: 15.0),
                                placeholder: 'Confirm password',
                                decoration: BoxDecoration(
                                    border:
                                        Border(bottom: BorderSide(width: .5)))),
                            android: (_) => MaterialTextFieldData(
                                decoration: InputDecoration(
                                    errorText: _confirmPasswordErrorText,
                                    labelText: 'Confirm password')),
                          ),
                          SizedBox(height: 40.0),
                          Center(
                            child: PlatformButton(
                              padding: EdgeInsets.symmetric(horizontal: 30.0),
                              ios: (_) => CupertinoButtonData(
                                  borderRadius: BorderRadius.circular(15.0)),
                              color: Colors.green,
                              onPressed: submit,
                              android: (_) => MaterialRaisedButtonData(
                                  padding: EdgeInsets.symmetric(
                                      vertical: 12.0, horizontal: 30.0),
                                  shape: StadiumBorder(),
                                  textColor: Colors.white),
                              child: PlatformText(
                                'Sign up',
                                style: TextStyle(fontSize: 16.0),
                              ),
                            ),
                          ),
                          SizedBox(height: 10.0),
                        ],
                      ),
                    ),
                  ],
                ),
                _isLoading
                    ? Container(
                        color: Colors.white,
                        child: Center(
                          child: CircularProgressIndicator(),
                        ),
                      )
                    : Container(),
              ],
            ),
          )),
      onWillPop: _onPop,
    );
  }

  Widget buildApp() {
    return PlatformScaffold(
        backgroundColor: Colors.white,
        body: ListView(
          children: <Widget>[
            buildAppBar(context),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 24.0),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  SizedBox(height: 5.0),
                  PlatformText(
                    'Register new account',
                    style:
                        TextStyle(fontSize: 24.0, fontWeight: FontWeight.bold),
                  ),
                  SizedBox(height: 15.0),
                  PlatformTextField(
                    controller: _emailController,
                    ios: (_) => CupertinoTextFieldData(
                        suffix: _emailErrorText == null
                            ? Container()
                            : Icon(Icons.error),
                        padding: EdgeInsets.symmetric(vertical: 15.0),
                        placeholder: 'email',
                        decoration: BoxDecoration(
                            border: Border(bottom: BorderSide(width: .5)))),
                    android: (_) => MaterialTextFieldData(
                        decoration: InputDecoration(
                            errorText: _emailErrorText, labelText: 'email')),
                  ),
                  SizedBox(height: 15.0),
                  PlatformTextField(
                    obscureText: true,
                    controller: _passwordController,
                    ios: (_) => CupertinoTextFieldData(
                        suffix: _passwordErrorText == null
                            ? Container()
                            : Icon(Icons.error),
                        padding: EdgeInsets.symmetric(vertical: 15.0),
                        placeholder: 'Password',
                        decoration: BoxDecoration(
                            border: Border(bottom: BorderSide(width: .5)))),
                    android: (_) => MaterialTextFieldData(
                        decoration: InputDecoration(
                            errorText: _passwordErrorText,
                            labelText: 'Password')),
                  ),
                  SizedBox(height: 15.0),
                  PlatformTextField(
                    obscureText: true,
                    controller: _confirmPasswordController,
                    ios: (_) => CupertinoTextFieldData(
                        suffix: _confirmPasswordErrorText == null
                            ? Container()
                            : Icon(Icons.error),
                        padding: EdgeInsets.symmetric(vertical: 15.0),
                        placeholder: 'Confirm password',
                        decoration: BoxDecoration(
                            border: Border(bottom: BorderSide(width: .5)))),
                    android: (_) => MaterialTextFieldData(
                        decoration: InputDecoration(
                            errorText: _confirmPasswordErrorText,
                            labelText: 'Confirm password')),
                  ),
                  SizedBox(height: 40.0),
                  Center(
                    child: PlatformButton(
                      padding: EdgeInsets.symmetric(horizontal: 30.0),
                      ios: (_) => CupertinoButtonData(
                          borderRadius: BorderRadius.circular(15.0)),
                      color: Colors.green,
                      onPressed: submit,
                      android: (_) => MaterialRaisedButtonData(
                          padding: EdgeInsets.symmetric(
                              vertical: 12.0, horizontal: 30.0),
                          shape: StadiumBorder(),
                          textColor: Colors.white),
                      child: PlatformText(
                        'Sign up',
                        style: TextStyle(fontSize: 16.0),
                      ),
                    ),
                  ),
                  SizedBox(height: 10.0),
                ],
              ),
            ),
          ],
        ));
  }

  Container buildAppBar(BuildContext context) {
    return Container(
      padding: EdgeInsets.only(left: 5.0),
      alignment: Alignment.centerLeft,
      height: 60.0,
      width: double.infinity,
      child: Row(
        children: <Widget>[
          PlatformIconButton(
            onPressed: () async {
              if (await _onPop()) {
                Navigator.pop(context);
              }
            },
            androidIcon: Icon(Icons.arrow_back),
            iosIcon: Icon(Icons.arrow_back_ios),
          ),
          Spacer(),
          // TODO show info
          PlatformIconButton(
            onPressed: () {},
            androidIcon: Icon(Icons.info_outline),
            iosIcon: Icon(CupertinoIcons.info),
          ),
        ],
      ),
    );
  }

  void submit() {
    bool validEmail = validateEmail(_emailController.text);
    bool validPassword = validatePassword(_passwordController.text);
    bool validConfirmPassword =
        validateConfirmPassword(_confirmPasswordController.text);

    InitialAuthenticationState theState =
        BlocProvider.of<AuthenticationBloc>(context).currentState;

    if (validEmail && validPassword && validConfirmPassword) {
      if (_passwordController.text == _confirmPasswordController.text) {
        if (_passwordController.text.length >= 8) {
          setState(() => _isLoading = true);
          BlocProvider.of<AuthenticationBloc>(context)
              .onSignUpButtonTapped(UserSignUpData(
            password: _passwordController.text,
            email: _emailController.text,
          ).toMap());
        } else {
          showCustomDialog(context, 'Error',
              'Password should be at least 8 characters long');
        }
      } else {
        showCustomDialog(context, 'Error', "Passwords don't match");
      }
    } else {
      showCustomDialog(context, 'Error',
          'Please check completed fields: ${validEmail ? '' : '\n email'}  ${validPassword ? '' : '\n Contraseña'} ${validConfirmPassword ? '' : '\n Confirm password'}');
    }
    Navigator.pop(context);
  }

  bool validateEmail(String text) {
    if (text.isEmpty) {
      setState(() => _emailErrorText = 'Compulsory field');
      return false;
    } else if (text.length < 6) {
      setState(() => _emailErrorText = 'Minimum length: 6 characters');
      return false;
//    } else if (RegExp(r"([0-9])").hasMatch(text)) {
//      setState(() => _emailErrorText = "An email can't have any numbers");
//      return false;
    } else {
      setState(() => _emailErrorText = null);
      return true;
    }
  }

  bool validatePassword(String text) {
    if (text.isEmpty) {
      setState(() => _passwordErrorText = 'Compulsory field');
      return false;
    } else if (text.length < 8) {
      setState(() => _passwordErrorText = 'Minimum length: 8 characters');
      return false;
    } else {
      setState(() => _passwordErrorText = null);
      return true;
    }
  }

  bool validateConfirmPassword(String text) {
    if (text.isEmpty) {
      setState(() => _confirmPasswordErrorText = 'Compulsory field');
      return false;
    } else if (text.length < 8) {
      setState(
          () => _confirmPasswordErrorText = 'Minimum length: 8 characters');
      return false;
    } else {
      setState(() => _confirmPasswordErrorText = null);
      return true;
    }
  }

  @override
  void dispose() {
    _emailController.dispose();
    _passwordController.dispose();
    _confirmPasswordController.dispose();
    super.dispose();
  }

  Future<bool> _onPop() async {
    return await showOnPopDialog(context);
  }
}
