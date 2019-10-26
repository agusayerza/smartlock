package com.smartlock.server.user.service;

import com.smartlock.server.lock.persistence.model.Lock;
import com.smartlock.server.lock.persistence.repository.LockRepository;
import com.smartlock.server.lock.presentation.dto.UserLockDto;
import com.smartlock.server.lock.presentation.dto.LockDto;
import com.smartlock.server.schedule.persistence.model.Schedule;
import com.smartlock.server.schedule.persistence.repository.ScheduleRepository;
import com.smartlock.server.security.service.UserPrinciple;
import com.smartlock.server.user.persistence.model.User;
import com.smartlock.server.user.persistence.repository.UserRepository;
import com.smartlock.server.user.presentation.dto.CreateUserDto;
import com.smartlock.server.user.presentation.dto.UserDto;
import com.smartlock.server.user.presentation.dto.UserWithoutLocksDto;
import com.smartlock.server.userValidator.service.UserValidatorService;
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
    private ScheduleRepository scheduleRepository;
    private UserValidatorService userValidatorService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, LockRepository lockRepository, UserValidatorService userValidatorService, ScheduleRepository scheduleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.lockRepository = lockRepository;
        this.scheduleRepository = scheduleRepository;
        this.userValidatorService = userValidatorService;
    }

    @Override
    public UserDto createUser(CreateUserDto userData) {
        if (userRepository.existsByEmail(userData.getEmail())) {
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
        if (!optionalUser.isPresent())throw new IllegalArgumentException("User not found");
        return new UserDto(optionalUser.get());
    }

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
        Lock lock = getLockIfPresentAndActive(lockId);
        if(lock.getUserAdminId() != userId) throw new IllegalArgumentException("Only lock admin can perform this operation");
        List<UserWithoutLocksDto> userDtoList = new ArrayList<>();
        List<User> userList = userRepository.findAllByLocksIdContaining(lock.getId());
        for (User user : userList) {
            userDtoList.add(new UserWithoutLocksDto(user));
        }
        return userDtoList;
    }

    @Override
    public void inviteUserToThisLock(UserLockDto userLockDto, Long userId) throws NotFoundException {
        Lock lock = getLockIfPresentAndActive(userLockDto.getLockId());
        if (lock.getUserAdminId() != userId)throw new IllegalArgumentException("You are not lock's admin. You can not add users to this lock");
        Optional<User> optionalUser = userRepository.findByEmail(userLockDto.getEmail());
        if(!optionalUser.isPresent()) throw new IllegalArgumentException("User does not exist");
        User user = optionalUser.get();
        if(user.getId() == userId) throw new IllegalArgumentException("Cannot invite yourself");
        userValidatorService.addValidationCode(userLockDto);
    }

    @Override
    public void removeUserToThisLock(UserLockDto userLockDto, Long userId) throws NotFoundException {
        Lock lock = getLockIfPresentAndActive(userLockDto.getLockId());
        if (lock.getUserAdminId() != userId) throw new IllegalArgumentException("You are not lock's admin. You can not remove users from this lock");
        Optional<User> optionalUser = userRepository.findByEmail(userLockDto.getEmail());
        if (!optionalUser.isPresent()) throw new IllegalArgumentException("User does not exist");
        User user = optionalUser.get();
        if (lock.getUserAdminId() == user.getId()) throw new IllegalArgumentException("You cannot remove yourself");
        if (!user.getLocksId().contains(userLockDto.getLockId())) throw new IllegalArgumentException("User cannot access that lock");
        List<Schedule> scheduleListOfThisUserAndThisLock = scheduleRepository.findAllByLockIdAndUserIdOrderByDayAsc(userLockDto.getLockId(), user.getId());
        for (Schedule schedule : scheduleListOfThisUserAndThisLock) {
            scheduleRepository.delete(schedule);
        }
        user.removeLock(lock.getId());
        userRepository.save(user);
    }

    @Override
    public void leaveFromThisLock(Long lockId, Long userId) throws NotFoundException {
        Lock lock = getLockIfPresentAndActive(lockId);
        if (lock.getUserAdminId() == userId) throw new IllegalArgumentException("You are the admin of this lock. Delete it, instead of leaving");
        User user = userRepository.getOne(userId);
        if (!user.getLocksId().contains(lockId)) throw new IllegalArgumentException("You cannot access that lock");
        List<Schedule> scheduleListOfThisUserAndThisLock = scheduleRepository.findAllByLockIdAndUserIdOrderByDayAsc(lockId, userId);
        for (Schedule schedule : scheduleListOfThisUserAndThisLock) {
            scheduleRepository.delete(schedule);
        }
        user.removeLock(lockId);
        userRepository.save(user);
    }

    private Lock getLockIfPresentAndActive(Long lockId) throws NotFoundException {
        Optional<Lock> optionalLock = lockRepository.findById(lockId);
        if (!optionalLock.isPresent()) throw new NotFoundException("Lock not found");
        Lock lock = optionalLock.get();
        if (!lock.isActive()) throw new NotFoundException("Lock not found");
        return lock;
    }
}
