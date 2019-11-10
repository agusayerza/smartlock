import 'dart:convert';
import 'dart:io';

import 'package:connectivity/connectivity.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:lock_app/bloc/authentication/authentication_bloc.dart';
import 'package:lock_app/bloc/authentication/authentication_state.dart';
import 'package:lock_app/data/user/user.dart';
import 'package:lock_app/globals.dart';
import 'package:http/http.dart' as http;

class HttpUserRepository {
  final AuthenticationBloc bloc;
  var connectivity = Connectivity();

  HttpUserRepository(this.bloc);

  //authentication

  Future<User> authenticate({
    @required String url,
    @required String username,
    @required String password,
  }) async {
    if (await _checkInternetConnectivity()) {
      http.Response response = await http.post(url,
          body: json.encode({"email": username, "password": password}),
          headers: {
            "Content-Type": "application/json"
          }).timeout(Duration(seconds: 3), onTimeout: () {
        return null;
      });

      if (response == null) {
        return throw TimeoutException();
      } else {
        print(response.body);
        var data = json.decode(response.body);
        final int statusCode = response.statusCode;
        if (statusCode < 200 || statusCode >= 400 || json == null) {
          return throw new InvalidCredentialsException();
        } else {
          return User.fromJson(data);
        }
      }
    } else {
      return throw ConnectionException();
    }
  }

  //new user

  Future<String> registerNewUser({String url, Map body}) async {
    if (await _checkInternetConnectivity()) {
      http.Response response = await http.post(url,
          body: json.encode(body),
          headers: {
            "Content-Type": "application/json"
          }).timeout(Duration(seconds: 5), onTimeout: () {
        return null;
      });

      if (response == null) {
        return throw TimeoutException();
      } else {
        print('Response is : $response');
        print('Response body is : ${response.body}');
        final int statusCode = response.statusCode;
        print(statusCode);
        if (statusCode < 200 || statusCode >= 400 || json == null) {
          print(response.body);
          return throw UserExistsException();
        } else {
          return 'token';
        }
      }
    } else {
      return throw ConnectionException();
    }
  }

  //delete user

  Future<String> deleteUser(String url, BuildContext context) async {
    AuthenticatedState state =
        BlocProvider.of<AuthenticationBloc>(context).currentState;

    return http.delete(Uri.encodeFull(url), headers: {
      HttpHeaders.authorizationHeader: 'Bearer ${state.token}',
    }).then((http.Response response) {
      print(response.body);
      final int statusCode = response.statusCode;
      print(statusCode);
      if (statusCode < 200 || statusCode > 400 || json == null) {
        throw new Exception("Error while fetching data");
      }
      return state.token;
    });
  }

// log out

  Future<String> logOut(String url, BuildContext context) async {
    AuthenticatedState state =
        BlocProvider.of<AuthenticationBloc>(context).currentState;

    return http.post(Uri.encodeFull(url), headers: {
      HttpHeaders.authorizationHeader: 'Bearer ${state.token}',
    }).then((http.Response response) {
      final int statusCode = response.statusCode;
      if (statusCode < 200 || statusCode > 400 || json == null) {
        throw new Exception("Error while fetching data");
      }
      return state.token;
    });
  }

//check internet

  Future<bool> _checkInternetConnectivity() async {
    var result = await connectivity.checkConnectivity();
    if (result == ConnectivityResult.none) {
      return false;
    } else {
      return true;
    }
  }

  static Future<int> getUser(context) {
    AuthenticatedState state =
        BlocProvider.of<AuthenticationBloc>(context).currentState;

    return http.get(Uri.encodeFull('http://' + ipAddress + '/users'), headers: {
      HttpHeaders.authorizationHeader: 'Bearer ${state.token}',
    }).then((http.Response response) {
      print(response.body);
      final int statusCode = response.statusCode;
      print(statusCode);
      if (statusCode < 200 || statusCode > 400 || json == null) {
        throw new Exception("Error while fetching data");
      }
      var body = jsonDecode(response.body);
      return body['id'];
    });
  }
}

//custom exceptions

class InvalidCredentialsException implements Exception {
  String errorMessage() {
    return 'Usuario o contraseña incorrecta';
  }
}

class TimeoutException implements Exception {
  String errorMessage() {
    return 'Timeout exception';
  }
}

class ConnectionException implements Exception {
  String errorMessage() {
    return 'Error de conexion';
  }
}

class UserExistsException implements Exception {
  String errorMessage() {
    return 'El correo electónico ya se encuentra en uso';
  }
}

class InvalidPasswordException implements Exception {
  String errorMessage() {
    return 'Contraseña incorrecta';
  }
}
