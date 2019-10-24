package com.smartlock.server.lock.presentation.controller;


import com.smartlock.server.lock.presentation.dto.CreateLockDto;
import com.smartlock.server.lock.service.LockService;
import com.smartlock.server.security.service.UserPrinciple;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/lock")
public class LockResource {
    private static Logger logger = LoggerFactory.getLogger(LockResource.class);

    private LockService lockService;
    private boolean bool = true;
    @Autowired
    public LockResource(LockService lockService) {
        this.lockService = lockService;
    }

//    todo verificar que user.getlocks contains lock y que este en dia y horario de schedule
    @PostMapping("/status/{uuid}")
    private String ping(@PathVariable String uuid){
        //logger.info(uuid);
        bool = !bool;
        if(bool){
            return "CLOSE";
        }
        return "OPEN";
    }

    @PostMapping()
    public ResponseEntity createLock(@Valid @RequestBody CreateLockDto lockDto){
        try {
            Long id = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(lockService.createLock(lockDto, id), HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLock(@PathVariable Long id) {
        try {
            Long userId = UserPrinciple.getUserPrinciple().getId();
            lockService.deleteLock(id, userId);
            return new ResponseEntity<>("Lock deleted", HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getLock(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(lockService.getLock(id), HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
