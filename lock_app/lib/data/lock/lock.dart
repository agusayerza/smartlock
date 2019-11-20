class Lock {
  final String name;
  final int id;
  final String uuid;
//  final int userAdminId;
  bool opened;

  Lock(this.name, this.id, this.uuid);

  Map dataToMap() {
    Map<String, dynamic> map = {
      "name": name,
      "id": id,
      "uuid": uuid,
//      "userAdminId": userAdminId,
      "opened": opened
    };
    return map;
  }

  Lock.fromMap(Map map)
      : name = map['name'],
        id = map['id'],
        uuid = map['uuid'],
//        userAdminId = map['userAdminId'],
        opened = map['opened'];
}
