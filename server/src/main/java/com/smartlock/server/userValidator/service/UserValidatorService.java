package com.smartlock.server.userValidator.service;

import com.smartlock.server.lock.presentation.dto.UserLockDto;
import com.smartlock.server.userValidator.presentation.dto.UserLockValidatorDto;
import javassist.NotFoundException;

public interface UserValidatorService {

    /**
     * Validates the code given by the user and add him to the lock if the invitation exists.
     * @param userLockValidatorDto DTO that contains the code tried to accept the invitation.
     * @param userId The ID of the User who sent the code to add the lock.
     * @throws NotFoundException thrown when the invitation does not exist.
     * */
    void validateUserAndLock(UserLockValidatorDto userLockValidatorDto, Long userId) throws NotFoundException;

    /**
     * Method used to invite a user into a lock, create and send the invitation email.
     * @param userLockDto {@code UserLockDto} DTO containing the information of the lock and user to be added to that lock.
     * @param lockAdminEmail String containing the email of the lock's admin.
     * @param lockName String containing lock's name.
     * */
    void addValidationCode(UserLockDto userLockDto, String lockAdminEmail, String lockName);

}
