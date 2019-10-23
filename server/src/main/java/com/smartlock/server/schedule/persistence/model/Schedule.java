package com.smartlock.server.schedule.persistence.model;

import com.smartlock.server.schedule.presentation.dto.CreateScheduleDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long lockId;
    private long userId;
    private long day;
    private LocalTime start;
    private LocalTime end;

    public Schedule(CreateScheduleDto createScheduleDto) {
        this.lockId = createScheduleDto.getLockId();
        this.userId = createScheduleDto.getUserId();
        this.day = createScheduleDto.getDay();
        this.start = createScheduleDto.getStart();
        this.end = createScheduleDto.getEnd();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLockId() {
        return lockId;
    }

    public void setLockId(long lockId) {
        this.lockId = lockId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }
}
