package com.smartlock.server.schedule.service;

import com.smartlock.server.lock.persistence.model.Lock;
import com.smartlock.server.lock.persistence.repository.LockRepository;
import com.smartlock.server.schedule.persistence.model.Schedule;
import com.smartlock.server.schedule.persistence.repository.ScheduleRepository;
import com.smartlock.server.schedule.presentation.dto.CreateScheduleDto;
import com.smartlock.server.schedule.presentation.dto.GetWeekScheduleDto;
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

        if(createScheduleDto.getStart().isAfter(createScheduleDto.getEnd())) throw new IllegalArgumentException("Start time must be before end time");

        Optional<User> optionalUser = userRepository.findByEmail(createScheduleDto.getEmail());
        if(!optionalUser.isPresent()) throw new NotFoundException("User not found");
        User user = optionalUser.get();
        if(user.getId() == userId) throw new IllegalArgumentException("Admin can access their lock at every moment");

        if(scheduleRepository.existsByUserIdAndLockIdAndDay(user.getId(), createScheduleDto.getLockId(), createScheduleDto.getDay())){
            throw new IllegalArgumentException("Schedule between that user and lock, for that day, already exists. Delete it before creating a new one");
        }

        Optional<Lock> optionalLock = lockRepository.findById(createScheduleDto.getLockId());
        if(!optionalLock.isPresent()) throw new NotFoundException("Lock not found");
        Lock lock = optionalLock.get();

        if (!checkIfUserIsAddedToLock(lock.getId(), user) || !checkIfIsAdminOfLock(lock, userId)) throw new IllegalArgumentException("You cannot perform this operation");

        Schedule schedule = new Schedule(createScheduleDto, user.getId());
        scheduleRepository.save(schedule);
        return new ScheduleDto(schedule);

    }

    private boolean checkIfUserIsAddedToLock(Long lockId, User user) {
        return user.getLocksId().contains(lockId);
    }

    private boolean checkIfIsAdminOfLock(Lock lock, Long userId) {
        return lock.getUserAdminId() == userId;
    }

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
    public List<ScheduleDto> getWeekScheduleOfThisUserAndLock(GetWeekScheduleDto getWeekScheduleDto, Long id) throws NotFoundException {
        Optional<Lock> optionalLock = lockRepository.findById(getWeekScheduleDto.getLockId());
        if (!optionalLock.isPresent()) throw new NotFoundException("Lock not found");
        Lock lock = optionalLock.get();
        if(lock.getUserAdminId() != id) throw new IllegalArgumentException("Only lock admin can perform this operation");
        Optional<User> optionalUser = userRepository.findByEmail(getWeekScheduleDto.getEmail());
        if(!optionalUser.isPresent()) throw new NotFoundException("User not found");
        User user = optionalUser.get();
        List<Schedule> weekSchedule = scheduleRepository.findAllByLockIdAndUserIdOrderByDayAsc(getWeekScheduleDto.getLockId(), user.getId());
        List<ScheduleDto> weekScheduleDto = new ArrayList<>();
        for (Schedule schedule : weekSchedule) {
            weekScheduleDto.add(new ScheduleDto(schedule));
        }
        return weekScheduleDto;
    }

}
