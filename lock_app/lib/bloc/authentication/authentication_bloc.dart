import 'package:bloc/bloc.dart';
import 'package:lock_app/data/user/user.dart';
import 'package:lock_app/repository/user_repository/http_user_repository.dart';
import 'package:lock_app/globals.dart';
import 'authentication_event.dart';
import 'authentication_state.dart';

class AuthenticationBloc
    extends Bloc<AuthenticationEvent, AuthenticationState> {
  static const LOGIN_POST_URL = BASE_URL + '/login';
  static const CREATE_POST_URL = BASE_URL + '/users';
  static const DELETE_USER_URL = BASE_URL + '/users/';
  static const LOG_OUT_URL = BASE_URL + '/logout';
  static String id;

  HttpUserRepository _userRepository;

  set userRepository(HttpUserRepository value) {
    _userRepository = value;
  }

  AuthenticationBloc();

  @override
  AuthenticationState get initialState => InitialAuthenticationState();

  @override
  Stream<AuthenticationState> mapEventToState(
      AuthenticationEvent event) async* {
    if (event is LogIn) {
      yield AuthLoadingState();
      try {
        User user = await _userRepository.authenticate(
            username: event.username,
            password: event.password,
            url: LOGIN_POST_URL);
        if (user == null) {
          yield AuthenticationError('Connection error');
        }
//        id = user.token;
        yield AuthenticatedState(user);
      } catch (e) {
        print(e.errorMessage());
        yield AuthenticationError(e.errorMessage());
      }
    } else if (event is LogOut) {
      AuthenticatedState logOutState = currentState;
      this._logOut(logOutState, event.context);
      yield AuthLoadingState();
    } else if (event is LogOutError) {
      yield LogOutErrorState('Error while logging out');
    } else if (event is LogOutSuccess) {
      yield InitialAuthenticationState();
    } else if (event is SignUp) {
      try {
        yield AuthLoadingState();
        print(event.body);
        User token = await _userRepository.registerNewUser(
            url: CREATE_POST_URL, body: event.body);
        // Await for back to send token
        //yield AuthenticatedState(token);

//        UserSignUpData data = UserSignUpData.fromMap(event.body);
//        dispatch(LogIn(data.username, data.password));
//        yield InitialAuthenticationState();
        yield InitialAuthenticationState();
      } catch (e) {
        print('$e');
        yield AuthenticationError(e.errorMessage());
      }
    } else if (event is DeleteUser) {
      AuthenticatedState deleteUserState = currentState;
      this._deleteUser(deleteUserState, event.context);
      yield AuthLoadingState();
    } else if (event is DeleteSuccess) {
      yield DeleteSuccessState('User deleted successfully');
    } else if (event is DeleteError) {
      yield DeleteErrorState('Error while deleting user');
    } else if (event is ModifyToken) {
      var temp = event.user;
      temp.token = event.token;
      yield AuthenticatedState(temp);
    } else if (event is Reset) {
      yield InitialAuthenticationState();
    }
  }

  _deleteUser(AuthenticatedState deleteUserState, context) async {
    try {
      _userRepository.deleteUser(DELETE_USER_URL, context);
      dispatch(DeleteSuccess());
    } catch (e) {
      dispatch(DeleteError());
    }
  }

  deleteUser(context) => dispatch(DeleteUser(context));

  onLoginButtonTapped(String username, String password) =>
      dispatch(LogIn(username, password));

  onLogOutButtonTapped(context) => dispatch(LogOut(context));

  _logOut(AuthenticatedState logOutState, context) {
    try {
      _userRepository.logOut(LOG_OUT_URL, context);
      dispatch(LogOutSuccess());
    } catch (e) {
      dispatch(LogOutError());
    }
  }

  setToken(String token, User user) {
    print(token);
    dispatch(ModifyToken(token, user));
  }

  onSignUpButtonTapped(Map body) => dispatch(SignUp(body));

  void reset() => dispatch(Reset());
}
