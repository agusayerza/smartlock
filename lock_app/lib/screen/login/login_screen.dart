import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_platform_widgets/flutter_platform_widgets.dart';
import 'package:lock_app/bloc/authentication/authentication_bloc.dart';
import 'package:lock_app/screen/register/register_screen.dart';

class LoginScreen extends StatefulWidget {
  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _mailController = TextEditingController();
  String _mailErrorText;

  String label;
  final _passwordController = TextEditingController();
  String _passwordErrorText;

  @override
  Widget build(BuildContext context) {
    return PlatformScaffold(
      body: Center(
        child: SingleChildScrollView(
          child: Padding(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                SizedBox(height: 50.0),
                Icon(Icons.lock, size: 100, color: Colors.green),
                SizedBox(height: 15.0),
                PlatformTextField(
                  controller: _mailController,
                  ios: (_) => CupertinoTextFieldData(
                      suffix: _mailErrorText == null
                          ? Container()
                          : Icon(Icons.error),
                      padding: EdgeInsets.symmetric(vertical: 15.0),
                      placeholder: 'Correo electrónico',
                      decoration: BoxDecoration(
                          border: Border(bottom: BorderSide(width: .5)))),
                  android: (_) => MaterialTextFieldData(
                      decoration: InputDecoration(
                          errorText: _mailErrorText,
                          labelText: 'Correo electrónico')),
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
                      placeholder: 'Contraseña',
                      decoration: BoxDecoration(
                          border: Border(bottom: BorderSide(width: .5)))),
                  android: (_) => MaterialTextFieldData(
                      decoration: InputDecoration(
                          errorText: _passwordErrorText,
                          labelText: 'Contraseña')),
                ),
                SizedBox(height: 20.0),
                PlatformButton(
                  padding: EdgeInsets.symmetric(horizontal: 30.0),
                  ios: (_) => CupertinoButtonData(
                      borderRadius: BorderRadius.circular(15.0)),
                  color: Colors.green,
                  onPressed: () => loginTapped(),
                  android: (_) => MaterialRaisedButtonData(
                      padding: EdgeInsets.symmetric(
                          vertical: 12.0, horizontal: 30.0),
                      shape: StadiumBorder(),
                      textColor: Colors.white),
                  child: PlatformText(
                    'INICIAR SESIÓN',
                    style: TextStyle(fontSize: 16.0),
                  ),
                ),
                SizedBox(height: 5.0),
                PlatformButton(
                  padding: EdgeInsets.symmetric(horizontal: 30.0),
                  onPressed: () => _openPage((_) => RegisterScreen()),
                  androidFlat: (_) => MaterialFlatButtonData(),
                  child: PlatformText(
                    'REGISTRARSE',
                    style: TextStyle(fontSize: 16.0),
                  ),
                ),
              ],
            ),
            padding: EdgeInsets.symmetric(horizontal: 30.0),
          ),
        ),
      ),
    );
  }

  _switchPlatform(BuildContext context) {
    if (isMaterial) {
      PlatformProvider.of(context).changeToCupertinoPlatform();
    } else {
      PlatformProvider.of(context).changeToMaterialPlatform();
    }
  }

  _openPage(WidgetBuilder pageToDisplayBuilder) {
    Navigator.push(
      context,
      platformPageRoute(
        builder: pageToDisplayBuilder,
      ),
    );
  }

  bool validateMail(String text) {
    if (text.isEmpty) {
      setState(() => _mailErrorText = 'Campo obligatorio');
      return false;
    } else if (!isValidEmail(text)) {
      setState(() => _mailErrorText = 'Formato de correo incorrecto');
      return false;
    } else {
      setState(() => _mailErrorText = null);
      return true;
    }
  }

  bool validatePassword(String text) {
    if (text.isEmpty) {
      setState(() => _passwordErrorText = 'Campo obligatorio');
      return false;
    } else if (text.length < 8) {
      setState(() => _passwordErrorText = 'Longitud mínima: 8 caracteres');
      return false;
    } else {
      setState(() => _passwordErrorText = null);
      return true;
    }
  }

  bool isValidEmail(String text) =>
      RegExp(r"^[a-zA-Z0-9.]+@[a-zA-Z0-9]+\.[a-zA-Z]+").hasMatch(text);

  @override
  void dispose() {
    _mailController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  loginTapped() {
    bool validUsername = validateMail(_mailController.text);
    bool validPassword = validatePassword(_passwordController.text);

    if (validUsername && validPassword) {
      BlocProvider.of<AuthenticationBloc>(context)
          .onLoginButtonTapped(_mailController.text, _passwordController.text);
    }
  }
}
