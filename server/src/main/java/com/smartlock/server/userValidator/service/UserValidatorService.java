package com.smartlock.server.userValidator.service;

import com.smartlock.server.lock.presentation.dto.UserLockDto;
import com.smartlock.server.userValidator.presentation.dto.UserLockValidatorDto;
import javassist.NotFoundException;

public interface UserValidatorService {


    void validateUserAndLock(UserLockValidatorDto userLockValidatorDto, Long userId) throws NotFoundException;

    void addValidationCode(UserLockDto userLockDto, String lockAdminEmail, String lockName);

}
