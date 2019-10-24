package com.smartlock.server.user.service;

import com.smartlock.server.lock.presentation.dto.UserLockDto;
import com.smartlock.server.lock.presentation.dto.LockDto;
import com.smartlock.server.user.presentation.dto.CreateUserDto;
import com.smartlock.server.user.presentation.dto.UserDto;
import com.smartlock.server.user.presentation.dto.UserWithoutLocksDto;
import javassist.NotFoundException;

import java.util.List;

public interface UserService {

    UserDto createUser(CreateUserDto userDto);

    UserDto getUser(Long id);

    Long getMyID();

//    todo te invitan y vos aceptas o te meten de una? en principio te meten de una, despues con mail

    List<UserWithoutLocksDto> getAllUsersThatCanAccessToThisLock(Long lockId, Long userId) throws NotFoundException;

    void addUserToThisLock(UserLockDto userLockDto, Long userId) throws NotFoundException;

    void removeUserToThisLock(UserLockDto userLockDto, Long userId) throws NotFoundException;

    void leaveFromThisLock(Long lockId, Long userId) throws NotFoundException;

    List<LockDto> getAllLocksThisUserCanAccess(Long userId);

    List<LockDto> getAllLocksIAmAdmin(Long userId);


}
