package com.smartlock.server.lock.service;

import com.smartlock.server.lock.persistence.model.Lock;
import com.smartlock.server.lock.persistence.repository.LockRepository;
import com.smartlock.server.lock.presentation.dto.CreateLockDto;
import com.smartlock.server.lock.presentation.dto.LockDto;
import com.smartlock.server.schedule.persistence.model.Schedule;
import com.smartlock.server.schedule.persistence.repository.ScheduleRepository;
import com.smartlock.server.schedule.service.ScheduleService;
import com.smartlock.server.user.persistence.model.User;
import com.smartlock.server.user.persistence.repository.UserRepository;
import com.smartlock.server.user.presentation.dto.UserDto;
import com.smartlock.server.user.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LockServiceImpl implements LockService{

    private LockRepository lockRepository;
    private UserRepository userRepository;
    private ScheduleRepository scheduleRepository;
    private ScheduleService scheduleService;
    private UserService userService;

    @Autowired
    public LockServiceImpl(UserRepository userRepository, UserService userService, LockRepository lockRepository, ScheduleRepository scheduleRepository, ScheduleService scheduleService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.lockRepository = lockRepository;
        this.scheduleRepository = scheduleRepository;
        this.scheduleService = scheduleService;
    }

    @Override
    public LockDto createLock(CreateLockDto lockDto, Long userAdminId) throws NotFoundException {
        User user = userRepository.getOne(userAdminId);
        Optional<Lock> opLock= lockRepository.findByUid(lockDto.getUid());
        if (opLock.isPresent()){
            Lock lock = opLock.get();
            if (lock.isActive()) throw new IllegalArgumentException("That lock is already claimed");
            lock.setActive(true);
            lock.setUserAdminId(userAdminId);
            user.addNewLock(lock.getId());
            lockRepository.save(lock);
            userRepository.save(user);
            return new LockDto(lock);
        }
        throw new NotFoundException("Lock not found");
    }

    @Override
    public void deleteLock(Long id, Long userId) throws NotFoundException {
        Optional<Lock> opLock = lockRepository.findById(id);
        if(opLock.isPresent()) {
            Lock lock = opLock.get();
            if (lock.getUserAdminId() == userId){
                List<UserDto> userDtos = userService.getAllUsersThatCanAccessToThisLock(id, userId);
                for (UserDto userDto : userDtos) {
                    User userOfLock = userRepository.getOne(userDto.getId());
                    userOfLock.removeLock(lock.getId());
                    userRepository.save(userOfLock);
                }

                List<Schedule> scheduleList = scheduleRepository.findAllByLockId(lock.getId());
                for (Schedule schedule : scheduleList) {
                    scheduleService.deleteSchedule(schedule.getId(), userId);
                }
                lock.setActive(false);
                lockRepository.save(lock);
                return;
            }
            throw new IllegalArgumentException("You are not lock's admin");
        }
        throw new NotFoundException("Lock not found");
    }

    @Override
    public LockDto getLock(Long id) throws NotFoundException {
        Optional<Lock> optionalLock = lockRepository.findById(id);
        if (optionalLock.isPresent()){
            Lock lock = optionalLock.get();
            if (lock.isActive()){
                return new LockDto(lock);
            }
        }
        throw new NotFoundException("Lock not found");
    }

}
