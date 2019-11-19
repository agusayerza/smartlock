import 'package:flutter/cupertino.dart';
import 'package:lock_app/data/user/user.dart';

@immutable
abstract class AuthenticationState {}

class InitialAuthenticationState extends AuthenticationState {}

class InitialRegistrationState extends AuthenticationState {}

class AuthLoadingState extends AuthenticationState {}

class AuthenticatedState extends AuthenticationState {
  final User user;

  AuthenticatedState(this.user);

  set token(String value) {
    user.token = value;
  }

  String get token => user.token;
}

class AuthenticationError extends AuthenticationState {
  final String _message;

  AuthenticationError(this._message);

  String get message => _message;
}

class LogOutErrorState extends AuthenticationState {
  final String _message;

  String get message => _message;

  LogOutErrorState(this._message);
}

class DeleteSuccessState extends AuthenticationState {
  final String _message;

  String get message => _message;

  DeleteSuccessState(this._message);
}

class DeleteErrorState extends AuthenticationState {
  final String _message;

  String get message => _message;

  DeleteErrorState(this._message);
}
