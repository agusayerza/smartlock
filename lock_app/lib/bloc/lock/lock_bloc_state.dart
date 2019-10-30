import 'package:flutter/cupertino.dart';
import 'package:lock_app/data/lock/lock.dart';

@immutable
abstract class LockBlocState {}

class InitialListLockState extends LockBlocState {}

class ListLockState extends LockBlocState {
  final List<Lock> locks;

  ListLockState(this.locks);

  List<Lock> get allLocks => null;
}

class AdminLockListState extends LockBlocState {
  final List<Lock> locks;

  AdminLockListState(this.locks);
}
