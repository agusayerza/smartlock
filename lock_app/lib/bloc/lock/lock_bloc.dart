import 'package:bloc/bloc.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:lock_app/data/lock/lock.dart';
import 'package:lock_app/repository/lock_repository/http_lock_repository.dart';
import 'lock_bloc_event.dart';
import 'lock_bloc_state.dart';

class LockBloc extends Bloc<LockBlocEvent, LockBlocState> {
  List<Lock> allLocks = [];
  int lockId;

  HttpLockRepository repository = HttpLockRepository();

  @override
  // TODO: implement initialState
  get initialState => ListLockState([]);

  @override
  Stream<LockBlocState> mapEventToState(LockBlocEvent event) async* {
    if (event is NewLockEvent) {
      try {
        await repository.registerNewLock(event.context, event.body);

        dispatch(ReloadLockListEvent(event.context, 'ok'));
      } catch (e) {
        print('${e.errorMessage()}');
        yield ListLockState((currentState as ListLockState).allLocks);
      }
    } else if (event is DeleteLockEvent) {
      try {
        await repository.deleteLock(event.context, event.lock.id);
      } catch (e) {
        print('${e.errorMessage()}');
      }
    } else if (event is ReloadLockListEvent) {
      final allLocksList = await repository.getMyLocks(event.context);
      yield ListLockState(allLocksList);
    }
//    else if (event is ReloadAdminLockListEvent) {
//      final adminLocksList = await repository.getMyAdminLocks(event.context);
//      yield AdminLockListState(adminLocksList);
//    }
  }

  deleteLockFromList(BuildContext context, Lock data) =>
      dispatch(DeleteLockEvent(context, data));

  loadLockList(context) => dispatch(ReloadLockListEvent(context, null));

  onNewLockTapped(BuildContext context, Map body) =>
      dispatch(NewLockEvent(context, body));

//  Lock get getLastLock {
//    return allLocks.last;
//  }
}
