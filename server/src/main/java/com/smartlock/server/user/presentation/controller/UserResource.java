package com.smartlock.server.user.presentation.controller;

import com.smartlock.server.lock.presentation.dto.AddUserToLockDto;
import com.smartlock.server.security.service.UserPrinciple;
import com.smartlock.server.user.presentation.dto.CreateUserDto;
import com.smartlock.server.user.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserResource {

    private UserService userService;

    @Autowired
    public UserResource(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        try {
            return new ResponseEntity<>(userService.createUser(createUserDto), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/me")
    public ResponseEntity getMyID(){
        return new ResponseEntity<>(userService.getMyID(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity getUser() {
        try {
            Long id = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/myLocks")
    public ResponseEntity getAllLockThisUserCanAccess() {
        try {
            Long id = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(userService.getAllLocksThisUserCanAccess(id), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/lock/{id}")
    public ResponseEntity getAllUsersThatCanAccessToThisLock(@PathVariable Long id){
        try {
            Long userId = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(userService.getAllUsersThatCanAccessToThisLock(id, userId), HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/lock")
    public ResponseEntity addUserToThisLock(@Valid @RequestBody AddUserToLockDto addUserToLockDto){
        try {
            Long userId = UserPrinciple.getUserPrinciple().getId();
            userService.addUserToThisLock(addUserToLockDto, userId);
            return new ResponseEntity<>("Used added", HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
