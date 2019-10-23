package com.smartlock.server.lock.service;

import com.smartlock.server.lock.presentation.dto.CreateLockDto;
import com.smartlock.server.lock.presentation.dto.LockDto;
import javassist.NotFoundException;


public interface LockService {

    LockDto createLock(CreateLockDto lockDto, Long userAdminId) throws NotFoundException;

    void deleteLock(Long id, Long userId) throws NotFoundException;

    LockDto getLock(Long id) throws NotFoundException;
}
