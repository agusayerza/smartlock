package com.smartlock.server.userValidator.service;

import com.smartlock.server.EmailServiceImpl;
import com.smartlock.server.lock.persistence.model.Lock;
import com.smartlock.server.lock.persistence.repository.LockRepository;
import com.smartlock.server.lock.presentation.dto.UserLockDto;
import com.smartlock.server.user.persistence.model.User;
import com.smartlock.server.user.persistence.repository.UserRepository;
import com.smartlock.server.userValidator.persistence.model.UserValidator;
import com.smartlock.server.userValidator.persistence.repository.UserValidatorRepository;
import com.smartlock.server.userValidator.presentation.dto.UserLockValidatorDto;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidatorServiceImpl implements UserValidatorService{

    private UserValidatorRepository userValidatorRepository;
    private UserRepository userRepository;
    private LockRepository lockRepository;

    /**
     * Class constructor
     * @param userValidatorRepository {@code UserValidatorRepository} instantiated class corresponding to the current Spring profile.
     * @param userRepository {@code UserRepository} instantiated class corresponding to the current Spring profile.
     * @param lockRepository {@code LockRepository} instantiated class corresponding to the current Spring profile.
     */
    @Autowired
    public UserValidatorServiceImpl(UserValidatorRepository userValidatorRepository, UserRepository userRepository, LockRepository lockRepository){
        this.userValidatorRepository = userValidatorRepository;
        this.userRepository = userRepository;
        this.lockRepository = lockRepository;
    }

    /**
     * Method used to invite a user into a lock, create and send the invitation email.
     * @param userLockDto {@code UserLockDto} DTO containing the information of the lock and user to be added to that lock.
     */
    public void addValidationCode(UserLockDto userLockDto) {
        UserValidator userValidator = new UserValidator(userLockDto.getEmail(), userLockDto.getLockId());
        userValidatorRepository.save(userValidator);
        EmailServiceImpl emailService = new EmailServiceImpl();
        emailService.sendSimpleMessage(userLockDto.getEmail(), "Lock invitation", "This is your validation code: " + userValidator.getCode());
    }

    /**
     * Validates the code given by the user and add him to the lock if the invitation exists.
     * @param userLockValidatorDto DTO that contains the code tried to accept the invitation.
     * @param userId The ID of the User who sent the code to add the lock.
     * @throws NotFoundException thrown when the invitation does not exist.
     */
    public void validateUserAndLock(UserLockValidatorDto userLockValidatorDto, Long userId) throws NotFoundException {
        User user = userRepository.getOne(userId);
        try {
            UserValidator userValidator = userValidatorRepository.findByEmailAndLockId(user.getEmail(), userLockValidatorDto.getLockId());
            if (!userValidator.getCode().equals(userLockValidatorDto.getCode()))
                throw new IllegalArgumentException("Invalid code");
            Lock lock = lockRepository.getOne(userLockValidatorDto.getLockId());
            user.addNewLock(lock.getId());
            userRepository.save(user);
            userValidatorRepository.delete(userValidator);
        }catch (NullPointerException e) {
            throw new NotFoundException("You were not invited to that lock");
        }
    }
}
