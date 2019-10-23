package com.smartlock.server.user.service;

import com.smartlock.server.lock.presentation.dto.AddUserToLockDto;
import com.smartlock.server.lock.presentation.dto.LockDto;
import com.smartlock.server.user.presentation.dto.CreateUserDto;
import com.smartlock.server.user.presentation.dto.UserDto;
import javassist.NotFoundException;

import java.util.List;

public interface UserService {

    UserDto createUser(CreateUserDto userDto);

    UserDto getUser(Long id);

    Long getMyID();

//    todo invitar a un user a su lock y sacarlo (verificar que sea el admin del lock)
//    todo te invitan y vos aceptas o te meten de una? en principio te meten de una, despues con mail

    List<UserDto> getAllUsersThatCanAccessToThisLock(Long lockId, Long userId) throws NotFoundException;

    void addUserToThisLock(AddUserToLockDto addUserToLockDto, Long userId) throws NotFoundException;

    List<LockDto> getAllLocksThisUserCanAccess(Long userId);


//    List<LockDto> get

//todo get all locks que soy admin
}
