package com.smartlock.server.schedule.presentation.controller;

import com.smartlock.server.schedule.presentation.dto.CreateScheduleDto;
import com.smartlock.server.schedule.service.ScheduleService;
import com.smartlock.server.security.service.UserPrinciple;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/schedule")
public class ScheduleResource {

    private ScheduleService scheduleService;

    @Autowired
    public ScheduleResource(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping()
    public ResponseEntity addNewSchedule(@Valid @RequestBody CreateScheduleDto scheduleDto){
        try {
            Long id = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(scheduleService.addNewSchedule(scheduleDto, id), HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping()
    public ResponseEntity deleteSchedule(@PathVariable Long id) {
        try {
            Long userId = UserPrinciple.getUserPrinciple().getId();
            scheduleService.deleteSchedule(id, userId);
            return new ResponseEntity<>("Schedule deleted", HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{userId}/lock/{lockID}")
    public ResponseEntity getWeekScheduleOfThisUserAndLock(@PathVariable Long userId, @PathVariable Long lockId) {
        try {
            Long id = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(scheduleService.getWeekScheduleOfThisUserAndLock(userId, lockId, id), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
