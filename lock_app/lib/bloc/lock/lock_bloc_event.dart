import 'package:flutter/cupertino.dart';
import 'package:lock_app/data/lock/lock.dart';

@immutable
abstract class LockBlocEvent {}

class ReloadLockListEvent extends LockBlocEvent {
  final BuildContext context;
  String msg;

  ReloadLockListEvent(this.context, this.msg);
}

class ReloadAdminLockListEvent extends LockBlocEvent {
  final BuildContext context;
  String msg;

  ReloadAdminLockListEvent(this.context, this.msg);
}

class NewLockEvent extends LockBlocEvent {
  final BuildContext context;
  final Map body;

  NewLockEvent(this.context, this.body);
}

class DeleteLockEvent extends LockBlocEvent {
  final BuildContext context;
  final Lock lock;

  DeleteLockEvent(this.context, this.lock);
}
