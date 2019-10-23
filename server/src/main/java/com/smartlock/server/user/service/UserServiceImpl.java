package com.smartlock.server.user.service;

import com.smartlock.server.lock.persistence.model.Lock;
import com.smartlock.server.lock.persistence.repository.LockRepository;
import com.smartlock.server.lock.presentation.dto.AddUserToLockDto;
import com.smartlock.server.lock.presentation.dto.LockDto;
import com.smartlock.server.security.service.UserPrinciple;
import com.smartlock.server.user.persistence.model.User;
import com.smartlock.server.user.persistence.repository.UserRepository;
import com.smartlock.server.user.presentation.dto.CreateUserDto;
import com.smartlock.server.user.presentation.dto.UserDto;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private LockRepository lockRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, LockRepository lockRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.lockRepository = lockRepository;
    }

    @Override
    public UserDto createUser(CreateUserDto userData) {
        if(userRepository.existsByEmail(userData.getEmail())){
            throw new IllegalArgumentException("El email esta en uso");
        }
        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
        User newUser = new User(userData);
        userRepository.save(newUser);
        return new UserDto(newUser);
    }

    @Override
    public UserDto getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new UserDto(user);
        }
        throw new IllegalArgumentException("No existe usuario con ese id");    }

    @Override
    public Long getMyID() {
        return UserPrinciple.getUserPrinciple().getId();
    }

    @Override
    public List<LockDto> getAllLocksThisUserCanAccess(Long userId) {
        User user = userRepository.getOne(userId);
        List<LockDto> lockDtoList = new ArrayList<>();
        for (Long lockId : user.getLocksId()) {
            lockDtoList.add(new LockDto(lockRepository.getOne(lockId)));
        }
        return lockDtoList;
    }

    @Override
    public List<UserDto> getAllUsersThatCanAccessToThisLock(Long lockId, Long userId) throws NotFoundException {
        Optional<Lock> optionalLock = lockRepository.findById(lockId);
        if (optionalLock.isPresent()){
            Lock lock = optionalLock.get();
            if (lock.isActive()){
                List<UserDto> userDtoList = new ArrayList<>();
                List<User> userList = userRepository.findAllByLocksIdContainingAndActiveTrue(lock.getId());
                for (User user : userList) {
                    userDtoList.add(new UserDto(user));
                }
                return userDtoList;
            }
        }
        throw new NotFoundException("Lock not found");
    }

    @Override
    public void addUserToThisLock(AddUserToLockDto addUserToLockDto, Long userId) throws NotFoundException {
        Optional<Lock> optionalLock = lockRepository.findById(addUserToLockDto.getLockId());
        if (optionalLock.isPresent()) {
            Lock lock = optionalLock.get();
            if (lock.getUserAdminId() == userId) {
                if (lock.isActive()) {
                    Optional<User> optionalUser = userRepository.findById(addUserToLockDto.getUserId());
                    if(optionalUser.isPresent()){
                        User user = optionalUser.get();
                        if (user.isActive()){
                            user.addNewLock(lock.getId());
                            userRepository.save(user);
                            return;
                        }
                    }
                    throw new IllegalArgumentException("User does not exists");
                }
                throw new NotFoundException("Lock not found");
            }
            throw new IllegalArgumentException("You are not lock's admin. You can not add users to this lock");
        }
        throw new NotFoundException("Lock not found");
    }
}
