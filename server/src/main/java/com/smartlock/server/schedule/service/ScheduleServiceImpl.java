package com.smartlock.server.schedule.service;

import com.smartlock.server.lock.persistence.model.Lock;
import com.smartlock.server.lock.persistence.repository.LockRepository;
import com.smartlock.server.schedule.persistence.model.Schedule;
import com.smartlock.server.schedule.persistence.repository.ScheduleRepository;
import com.smartlock.server.schedule.presentation.dto.CreateScheduleDto;
import com.smartlock.server.schedule.presentation.dto.ScheduleDto;
import com.smartlock.server.user.persistence.model.User;
import com.smartlock.server.user.persistence.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService{

    private ScheduleRepository scheduleRepository;
    private UserRepository userRepository;
    private LockRepository lockRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, UserRepository userRepository, LockRepository lockRepository) {
        this.scheduleRepository = scheduleRepository;
        this.lockRepository = lockRepository;
        this.userRepository = userRepository;
    }


    @Override
    public ScheduleDto addNewSchedule(CreateScheduleDto createScheduleDto, Long userId) throws NotFoundException {
        if(scheduleRepository.existsByUserIdAndLockId(createScheduleDto.getUserId(), createScheduleDto.getLockId())){
            throw new IllegalArgumentException("Schedule between that user and lock already exists. Delete it before creating a new one");
        }
        Optional<Lock> optionalLock = lockRepository.findById(createScheduleDto.getLockId());
        Optional<User> optionalUser = userRepository.findById(createScheduleDto.getUserId());

        if(!optionalLock.isPresent()) throw new NotFoundException("Lock not found");
        if(!optionalUser.isPresent()) throw new NotFoundException("User not found");

        Lock lock = optionalLock.get();
        User user = optionalUser.get();

        if (!checkIfUserIsAddedToLock(lock.getId(), user) && !checkIfIsAdminOfLock(lock, userId)) throw new IllegalArgumentException("You cannot perform this operation");

        Schedule schedule = new Schedule(createScheduleDto);
        scheduleRepository.save(schedule);
        return new ScheduleDto(schedule);

    }

    private boolean checkIfUserIsAddedToLock(Long lockId, User user) {
        return user.getLocksId().contains(lockId);
    }

    private boolean checkIfIsAdminOfLock(Lock lock, Long userId) {
        return lock.getUserAdminId() == userId;
    }

//  todo borrar todos los horarios que tengan ese candado cuando borro un lock
    @Override
    public void deleteSchedule(Long scheduleId, Long userId) throws NotFoundException {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
        if(optionalSchedule.isPresent()) {
            Schedule schedule = optionalSchedule.get();
            Lock lock = lockRepository.getOne(schedule.getLockId());

            if(checkIfIsAdminOfLock(lock, userId)) throw new IllegalArgumentException("You cannot perform this operation");
            scheduleRepository.delete(schedule);
            return;
            }
        throw new NotFoundException("Schedule not found");
    }

    @Override
    public List<ScheduleDto> getWeekScheduleOfThisUserAndLock(Long userId, Long lockId, Long id) {
        List<Schedule> weekSchedule = scheduleRepository.findAllByLockIdAndUserIdOrderByDayAsc(lockId, userId);
        List<ScheduleDto> weekScheduleDto = new ArrayList<>();
        for (Schedule schedule : weekSchedule) {
            weekScheduleDto.add(new ScheduleDto(schedule));
        }
        return weekScheduleDto;
    }

}
