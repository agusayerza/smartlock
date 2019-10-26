package com.smartlock.server.lock.presentation.controller;


import com.smartlock.server.lock.presentation.dto.CreateLockDto;
import com.smartlock.server.lock.service.LockService;
import com.smartlock.server.security.service.UserPrinciple;
import io.swagger.annotations.Api;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/lock")
public class LockResource {
    private static Logger logger = LoggerFactory.getLogger(LockResource.class);

    private LockService lockService;

    @Autowired
    public LockResource(LockService lockService) {
        this.lockService = lockService;
    }

    @ApiIgnore
    @PostMapping("/status/{uuid}") // todo: not really a post, should be get
    private String getLockStatus(@PathVariable String uuid){
        try{
            return lockService.getLockStatus(uuid);
        }catch (NotFoundException e){
            return e.getMessage();
        }
    }

    @PostMapping("/open/{uuid}")
    private String openLock(@PathVariable String uuid){
        // todo: validacion de permisos
        try{
            return lockService.getSetLockOpen(uuid, true);
        }catch (NotFoundException e){
            return e.getMessage();
        }
    }

    @PostMapping("/close/{uuid}")
    private String closeLock(@PathVariable String uuid){
        // todo: validacion de permisos, en el mismo metodo
        try{
            return lockService.getSetLockOpen(uuid, false);
        }catch (NotFoundException e){
            return e.getMessage();
        }
    }

    @PostMapping()
    public ResponseEntity addLock(@Valid @RequestBody CreateLockDto lockDto){
        try {
            Long id = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(lockService.addLock(lockDto, id), HttpStatus.OK);
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

    //  todo verificar que user.getlocks contains lock y que este en dia y horario de schedule
    @GetMapping("/{id}")
    public ResponseEntity getLock(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(lockService.getLock(id), HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
