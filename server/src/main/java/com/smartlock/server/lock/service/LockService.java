package com.smartlock.server.lock.service;

import com.smartlock.server.lock.presentation.dto.AddUserToLockDto;
import com.smartlock.server.lock.presentation.dto.CreateLockDto;
import com.smartlock.server.lock.presentation.dto.LockDto;
import com.smartlock.server.user.presentation.dto.UserDto;
import javassist.NotFoundException;

import java.util.List;

public interface LockService {

    LockDto createLock(CreateLockDto lockDto, Long userAdminId);

    //    todo cuando borro un user, que pasa con los locks del que era admin?
    void deleteLock(Long id, Long userId) throws NotFoundException;

    LockDto getLock(Long id) throws NotFoundException;

    List<UserDto> getAllUsersThatCanAccessToThisLock(Long lockId, Long userId) throws NotFoundException;

    void addUserToThisLock(AddUserToLockDto addUserToLockDto, Long userId) throws NotFoundException;
}
