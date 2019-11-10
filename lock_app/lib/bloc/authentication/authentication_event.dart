import 'package:flutter/cupertino.dart';
import 'package:lock_app/data/user/user.dart';

@immutable
abstract class AuthenticationEvent {}

class LogIn extends AuthenticationEvent {
  final String _username;
  final String _password;

  LogIn(this._username, this._password);

  String get username => _username;

  String get password => _password;
}

class SignUp extends AuthenticationEvent {
  final Map _body;

  SignUp(this._body);

  Map get body => _body;
}

class LogOut extends AuthenticationEvent {
  final BuildContext context;

  LogOut(this.context);
}

class LogOutSuccess extends AuthenticationEvent {}

class LogOutError extends AuthenticationEvent {}

class DeleteUser extends AuthenticationEvent {
  final BuildContext context;

  DeleteUser(this.context);
}

class DeleteSuccess extends AuthenticationEvent {}

class DeleteError extends AuthenticationEvent {}

class ModifyToken extends AuthenticationEvent {
  final String token;
  final User user;

  ModifyToken(this.token, this.user);
}

class Reset extends AuthenticationEvent {}
