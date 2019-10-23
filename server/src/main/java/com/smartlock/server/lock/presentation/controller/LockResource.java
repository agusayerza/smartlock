package com.smartlock.server.lock.presentation.controller;


import com.smartlock.server.lock.presentation.dto.AddUserToLockDto;
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

    @Autowired
    public LockResource(LockService lockService) {
        this.lockService = lockService;
    }

//    @PostMapping
//    private String ping(String ping){
//        logger.info(ping);
//        return "pong";
//    }

    @PostMapping()
    public ResponseEntity createLock(@Valid @RequestBody CreateLockDto lockDto){
        try {
            Long id = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(lockService.createLock(lockDto, id), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//    //    todo cuando borro un user, que pasa con los locks del que era admin?
//    @DeleteMapping("/{id}")
//    public ResponseEntity deleteLock(@PathVariable Long id) {
//        try {
//            Long userId = UserPrinciple.getUserPrinciple().getId();
////            todo hacer que solo el admin lo pueda eliminar??
//            lockService.deleteLock(id, userId);
//            return new ResponseEntity<>("Candado eliminado", HttpStatus.OK);
//        } catch (NotFoundException | IllegalArgumentException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity getLock(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(lockService.getLock(id), HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//    TODO ESTOS DOS, ACA O EN USER SERVICE? pasar a user
    @GetMapping("/users/{id}")
    public ResponseEntity getAllUsersThatCanAccessToThisLock(@PathVariable Long id){
        try {
            Long userId = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(lockService.getAllUsersThatCanAccessToThisLock(id, userId), HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users/")
    public ResponseEntity addUserToThisLock(@Valid @RequestBody AddUserToLockDto addUserToLockDto){
        try {
            Long userId = UserPrinciple.getUserPrinciple().getId();
            lockService.addUserToThisLock(addUserToLockDto, userId);
            return new ResponseEntity<>("Usuario agregado", HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
