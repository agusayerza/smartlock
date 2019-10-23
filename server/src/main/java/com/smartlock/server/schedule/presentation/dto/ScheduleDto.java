package com.smartlock.server.schedule.presentation.dto;

import com.smartlock.server.schedule.persistence.model.Schedule;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;

public class ScheduleDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long lockId;
    private long userId;
    private String day;
    private LocalTime start;
    private LocalTime end;

    public ScheduleDto(Schedule schedule) {
        this.id = schedule.getId();
        this.lockId = schedule.getLockId();
        this.userId = schedule.getUserId();
        this.start = schedule.getStart();
        this.end = schedule.getEnd();
        this.day = getDayString(schedule.getDay());
    }

    private String getDayString(long day) {
        switch ((int) day) {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            case 7:
                return "Sunday";
        }
        return "Error";
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
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
