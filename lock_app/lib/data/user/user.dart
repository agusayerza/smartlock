class User {
  String mail, token;
  final int id;
  User({this.mail, this.token, this.id});

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
        mail: json['email'], token: json['accessToken'], id: json['id']);
  }
}
