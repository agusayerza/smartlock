import 'package:lock_app/data/user/user.dart';

class Group {
  //falta el owner del group?
  List<User> users;
  final int id;

  Group({this.users, this.id});

  factory Group.fromJson(Map<String, dynamic> json) {
    return Group(users: json['users'], id: json['id']);
  }

  //para agregar usuarios al grupo, creo un grupo con la lista vacia y despues voy haciendo invites? o los addeo directamente yo o algo asi
}
