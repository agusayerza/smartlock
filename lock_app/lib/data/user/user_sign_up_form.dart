class UserSignUpData {
  final String email, password;

  UserSignUpData({
    this.email,
    this.password,
  });

  UserSignUpData.fromMap(map)
      : password = map['password'],
        email = map['email'];

  Map toMap() {
    Map<String, dynamic> userMap = {
      "email": email,
      "password": password,
    };
    return userMap;
  }
}
