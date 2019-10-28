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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UserValidatorServiceImpl implements UserValidatorService{

    private UserValidatorRepository userValidatorRepository;
    private UserRepository userRepository;
    private LockRepository lockRepository;

    @Autowired
    public UserValidatorServiceImpl(UserValidatorRepository userValidatorRepository, UserRepository userRepository, LockRepository lockRepository){
        this.userValidatorRepository = userValidatorRepository;
        this.userRepository = userRepository;
        this.lockRepository = lockRepository;
    }

    public void addValidationCode(UserLockDto userLockDto, String lockAdminEmail, String lockName) {
        if (userValidatorRepository.existsByLockIdAndEmail(userLockDto.getLockId(), userLockDto.getEmail())) throw new IllegalArgumentException("User already invited");
        UserValidator userValidator = new UserValidator(userLockDto.getEmail(), userLockDto.getLockId());
        userValidatorRepository.save(userValidator);
        EmailServiceImpl emailService = new EmailServiceImpl();
        emailService.sendSimpleMessage(userLockDto.getEmail(), "Lock invitation", "You have been invited by user " + lockAdminEmail + " to access to the lock named " + lockName +"\nThis is your validation code: " + userValidator.getCode());
    }

    public void validateUserAndLock(UserLockValidatorDto userLockValidatorDto, Long userId) throws NotFoundException {
        User user = userRepository.getOne(userId);
        try {
            UserValidator userValidator = userValidatorRepository.findByCode(userLockValidatorDto.getCode());
            if (!userValidator.getCode().equals(userLockValidatorDto.getCode()))
                throw new IllegalArgumentException("Invalid code");
            Lock lock = lockRepository.getOne(userValidator.getLockId());
            user.addNewLock(lock.getId());
            userRepository.save(user);
            userValidatorRepository.delete(userValidator);
        }catch (NullPointerException e) {
            throw new NotFoundException("You were not invited to that lock");
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void removeValidationCode(){
        userValidatorRepository.deleteAll();
    }
}
