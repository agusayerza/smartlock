package com.smartlock.server.user.service;

import com.smartlock.server.lock.presentation.dto.LockDto;
import com.smartlock.server.user.presentation.dto.CreateUserDto;
import com.smartlock.server.user.presentation.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(CreateUserDto userDto);

//    todo cuando borro un user, que pasa con los locks del que era admin?
//    void deleteUser(Long id);

    UserDto getUser(Long id);

    Long getMyID();

    List<LockDto> getAllLocksThisUserCanAccess(Long userId);
//    todo invitar a un user a su lock y sacarlo (verificar que sea el admin del lock)
//    todo te invitan y vos aceptas o te meten de una? en principio te meten de una, despues con mail


//todo get all locks que soy admin
}
