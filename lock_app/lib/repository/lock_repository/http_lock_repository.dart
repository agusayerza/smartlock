import 'dart:convert';
import 'dart:io';

import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:lock_app/bloc/authentication/authentication_bloc.dart';
import 'package:lock_app/bloc/authentication/authentication_state.dart';
import 'package:http/http.dart' as http;
import 'package:lock_app/data/lock/lock.dart';
import 'package:lock_app/globals.dart';
import 'package:lock_app/repository/user_repository/http_user_repository.dart';

class HttpLockRepository {
  Future registerNewLock(context, Map body) async {
    AuthenticatedState state =
        BlocProvider.of<AuthenticationBloc>(context).currentState;
    http.Response response =
        await http.post(BASE_URL + '/lock', body: json.encode(body), headers: {
      HttpHeaders.authorizationHeader: 'Bearer ${state.token}',
      "Content-Type": "application/json",
    }).timeout(Duration(seconds: 5), onTimeout: () {
      return null;
    });

    if (response == null) {
      return throw TimeoutException();
    } else {
      print('Response body is : ${response.body}');

      if (response.statusCode == HttpStatus.ok) {
        print('Agregado');
      } else {
        print(response.body);
        return throw TimeoutException();
      }
    }
  }

  Future<List<Lock>> getMyAdminLocks(context) async {
    AuthenticatedState state =
        BlocProvider.of<AuthenticationBloc>(context).currentState;
    http.Response response = await http.get(BASE_URL + '/', headers: {
      HttpHeaders.authorizationHeader: 'Bearer ${state.token}',
      "Content-Type": "application/json",
    }).timeout(Duration(seconds: 5), onTimeout: () {
      return null;
    });

    if (response == null) {
      return throw TimeoutException();
    } else {
      print('Response body is : ${response.body}');
      if (response.statusCode == HttpStatus.ok) {
        Iterable i = json.decode(response.body);
        List<Lock> list = [];
        i.forEach((m) {
          if (m['active']) {
            list.add(Lock.fromMap(m));
          }
        });
        return list;
      } else {
        print(response.body);
        return throw Exception(response.body);
      }
    }
  }

  Future<List<Lock>> getMyLocks(context) async {
    AuthenticatedState state =
        BlocProvider.of<AuthenticationBloc>(context).currentState;
    http.Response response =
        await http.get(BASE_URL + '/users/myLocks', headers: {
      HttpHeaders.authorizationHeader: 'Bearer ${state.token}',
      "Content-Type": "application/json",
    }).timeout(Duration(seconds: 5), onTimeout: () {
      return null;
    });

    if (response == null) {
      return throw TimeoutException();
    } else {
      print('Response body is : ${response.body}');
      if (response.statusCode == HttpStatus.ok) {
        Iterable i = json.decode(response.body);
        List<Lock> list = [];
        i.forEach((m) {
          list.add(Lock.fromMap(m));
        });
        return list;
      } else {
        print(response.body);
        return throw Exception(response.body);
      }
    }
  }

  deleteLock(context, id) async {
    AuthenticatedState state =
        BlocProvider.of<AuthenticationBloc>(context).currentState;
    http.Response response =
        await http.delete(BASE_URL + '/users/lock/$id', headers: {
      "Content-Type": "application/json",
      HttpHeaders.authorizationHeader: 'Bearer ${state.token}',
    }).timeout(Duration(seconds: 5), onTimeout: () {
      return null;
    });

    if (response == null) {
      return throw TimeoutException();
    } else {
      print('Response body is : ${response.body}');

      if (response.statusCode == HttpStatus.ok) {
        print('Todo OK');
      } else {
        print(response.body);
        return throw TimeoutException();
      }
    }
  }

  static addUserToThisLock(context, Map body) async {
    AuthenticatedState state =
        BlocProvider.of<AuthenticationBloc>(context).currentState;
    String test = json.encode(body);
    print(test + 'aaaaaabbbbbbbb');
    http.Response response =
        await http.put(BASE_URL + '/users/lock', body: test, headers: {
      "Content-Type": "application/json",
      HttpHeaders.authorizationHeader: 'Bearer ${state.token}',
    });
    print(response.body);
    print("cccccccccccccccc");
    if (response == null) {
      return throw TimeoutException();
    } else {
      var data = json.decode(response.body);
      print(data);
      final int statusCode = response.statusCode;
      if (statusCode == 200) {
        print("OK");
      }
    }
  }

  static openLock(uuid, context) async {
    AuthenticatedState state =
        BlocProvider.of<AuthenticationBloc>(context).currentState;

    http.Response response =
        await http.post(BASE_URL + '/lock/open/$uuid', headers: {
      HttpHeaders.authorizationHeader: 'Bearer ${state.token}',
      "Content-Type": "application/json",
    }).timeout(Duration(seconds: 5), onTimeout: () {
      return null;
    });

    if (response == null) {
      return throw TimeoutException();
    } else {
      print('Response body is : ${response.body}');

      if (response.statusCode == HttpStatus.ok) {
        print('${response.statusCode} openlock');
      } else {
        print(response.body);
        return throw TimeoutException();
      }
    }
  }

  static closeLock(uuid, context) async {
    AuthenticatedState state =
        BlocProvider.of<AuthenticationBloc>(context).currentState;

    http.Response response =
        await http.post(BASE_URL + '/lock/close/$uuid', headers: {
      HttpHeaders.authorizationHeader: 'Bearer ${state.token}',
      "Content-Type": "application/json",
    }).timeout(Duration(seconds: 5), onTimeout: () {
      return null;
    });

    if (response == null) {
      return throw TimeoutException();
    } else {
      print('Response body is : ${response.body}');

      if (response.statusCode == HttpStatus.ok) {
        print('${response.statusCode} closelock');
      } else {
        print(response.body);
        return throw TimeoutException();
      }
    }
  }
}

class TimeoutException implements Exception {
  String errorMessage() {
    return 'Timeout exception';
  }
}
