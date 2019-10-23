package com.smartlock.server.schedule.service;

import com.smartlock.server.schedule.presentation.dto.CreateScheduleDto;
import com.smartlock.server.schedule.presentation.dto.ScheduleDto;
import javassist.NotFoundException;

import java.util.List;

public interface ScheduleService {

    ScheduleDto addNewSchedule(CreateScheduleDto createScheduleDto, Long userId) throws NotFoundException;

    void deleteSchedule(Long scheduleId, Long userId) throws NotFoundException;

    List<ScheduleDto> getWeekScheduleOfThisUserAndLock(Long userId, Long lockId, Long id);

}
