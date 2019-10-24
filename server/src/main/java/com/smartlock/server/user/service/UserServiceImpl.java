package com.smartlock.server.user.service;

import com.smartlock.server.lock.persistence.model.Lock;
import com.smartlock.server.lock.persistence.repository.LockRepository;
import com.smartlock.server.lock.presentation.dto.UserLockDto;
import com.smartlock.server.lock.presentation.dto.LockDto;
import com.smartlock.server.security.service.UserPrinciple;
import com.smartlock.server.user.persistence.model.User;
import com.smartlock.server.user.persistence.repository.UserRepository;
import com.smartlock.server.user.presentation.dto.CreateUserDto;
import com.smartlock.server.user.presentation.dto.UserDto;
import com.smartlock.server.user.presentation.dto.UserWithoutLocksDto;
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
            throw new IllegalArgumentException("Email already in use");
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
        throw new IllegalArgumentException("User not found");    }

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
    public List<LockDto> getAllLocksIAmAdmin(Long userId) {
        List<Lock> lockList = lockRepository.findAllByUserAdminIdAndActiveTrue(userId);
        List<LockDto> lockDtoList = new ArrayList<>();
        for (Lock lock : lockList) {
            lockDtoList.add(new LockDto(lock));
        }
        return lockDtoList;
    }

    @Override
    public List<UserWithoutLocksDto> getAllUsersThatCanAccessToThisLock(Long lockId, Long userId) throws NotFoundException {
        Optional<Lock> optionalLock = lockRepository.findById(lockId);
        if (!optionalLock.isPresent()) throw new NotFoundException("Lock not found");
        Lock lock = optionalLock.get();
        if (!lock.isActive()) throw new NotFoundException("Lock not found");
        if(lock.getUserAdminId() != userId) throw new IllegalArgumentException("Only lock admin can perform this operation");
        List<UserWithoutLocksDto> userDtoList = new ArrayList<>();
        List<User> userList = userRepository.findAllByLocksIdContaining(lock.getId());
        for (User user : userList) {
            userDtoList.add(new UserWithoutLocksDto(user));
        }
        return userDtoList;
    }

    @Override
    public void addUserToThisLock(UserLockDto userLockDto, Long userId) throws NotFoundException {
        Optional<Lock> optionalLock = lockRepository.findById(userLockDto.getLockId());
        if (!optionalLock.isPresent()) throw new NotFoundException("Lock not found");
        Lock lock = optionalLock.get();
        if (lock.getUserAdminId() != userId)throw new IllegalArgumentException("You are not lock's admin. You can not add users to this lock");
        if (!lock.isActive()) throw new NotFoundException("Lock not found");
        Optional<User> optionalUser = userRepository.findByEmail(userLockDto.getEmail());
        if(!optionalUser.isPresent()) throw new IllegalArgumentException("User does not exist");
        User user = optionalUser.get();
        if(userLockDto.getEmail().equals(user.getEmail())) throw new IllegalArgumentException("Cannot add yourself");
        user.addNewLock(lock.getId());
        userRepository.save(user);
    }

    @Override
    public void removeUserToThisLock(UserLockDto userLockDto, Long userId) throws NotFoundException {
        Optional<Lock> optionalLock = lockRepository.findById(userLockDto.getLockId());
        if (!optionalLock.isPresent()) throw new NotFoundException("Lock not found");
        Lock lock = optionalLock.get();
        if (lock.getUserAdminId() != userId) throw new IllegalArgumentException("You are not lock's admin. You can not remove users from this lock");
        if (!lock.isActive()) throw new NotFoundException("Lock not found");
        Optional<User> optionalUser = userRepository.findByEmail(userLockDto.getEmail());
        if (!optionalUser.isPresent()) throw new IllegalArgumentException("User does not exist");
        if (lock.getUserAdminId() == userId) throw new IllegalArgumentException("You cannot remove yourself");
        User user = optionalUser.get();
        user.removeLock(lock.getId());
        userRepository.save(user);
    }

    @Override
    public void leaveFromThisLock(Long lockId, Long userId) throws NotFoundException {
        Optional<Lock> optionalLock = lockRepository.findById(lockId);
        if (!optionalLock.isPresent()) throw new NotFoundException("Lock not found");
        Lock lock = optionalLock.get();
        if (!lock.isActive()) throw new NotFoundException("Lock not found");
        if (lock.getUserAdminId() == userId) throw new IllegalArgumentException("You are the admin of this lock. Delete it, instead of leaving");
        User user = userRepository.getOne(userId);
        user.removeLock(lockId);
        userRepository.save(user);
    }
}
