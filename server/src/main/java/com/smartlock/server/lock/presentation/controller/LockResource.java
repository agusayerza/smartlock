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
    private ResponseEntity openLock(@PathVariable String uuid){
        try{
            Long id = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(lockService.getSetLockOpen(uuid, true, id), HttpStatus.OK);
        }catch (NotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/close/{uuid}")
    private ResponseEntity closeLock(@PathVariable String uuid){
        try{
            Long id = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(lockService.getSetLockOpen(uuid, false, id), HttpStatus.OK);
        }catch (NotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint used to claim a lock, making the request user lock's admin
     * @param lockDto DTO containing Lock uuid and name for the lock
     * @return returns ResponseEntity, OK if successful, BAD_REQUEST if it failed.
     */
    @PostMapping()
    public ResponseEntity addLock(@Valid @RequestBody CreateLockDto lockDto){
        try {
            Long id = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(lockService.addLock(lockDto, id), HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//    /**
//     * Endpoint to delete a lock, making it's active variable as false
//     * @param id of the lock to be deleted
//     * @return returns ResponseEntity, OK if successful, BAD_REQUEST if it failed.
//     */
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity deleteLock(@PathVariable Long id) {
//        try {
//            Long userId = UserPrinciple.getUserPrinciple().getId();
//            lockService.deleteLock(id);
//            return new ResponseEntity<>("Lock deleted", HttpStatus.OK);
//        } catch (NotFoundException | IllegalArgumentException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

    /**
     * Endpoint to get data of the lock
     * @param id of the lock data requested
     * @return returns ResponseEntity, OK if successful, BAD_REQUEST if it failed.
     */
    @GetMapping("/{id}")
    public ResponseEntity getLock(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(lockService.getLock(id), HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
