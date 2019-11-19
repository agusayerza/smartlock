package com.smartlock.server.user.presentation.controller;

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

    /**
     * Endpoint used to post the data corresponding to a new user and create it.
     * @param createUserDto DTO that contains the user to be created data.
     * @return {@code ResponseEntity}, OK if successful, BAD_REQUEST if it failed.
     */
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        try {
            return new ResponseEntity<>(userService.createUser(createUserDto), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint used to get my own User Id.
     * @return {@code ResponseEntity} OK, containing the User's Id.
     */
    @GetMapping("/me")
    public ResponseEntity getMyID(){
        return new ResponseEntity<>(userService.getMyID(), HttpStatus.OK);
    }

    /**
     * Endpoint used to get my own UserData.
     * @return {@code ResponseEntity}, OK if successful, BAD_REQUEST if it failed.
     */
    @GetMapping()
    public ResponseEntity getUser() {
        try {
            Long id = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint to get a List for all the {@code Lock}s a {@code User} can access.
     * @return {@code ResponseEntity}, OK if successful, BAD_REQUEST if it failed.
     */
    @GetMapping("/myLocks")
    public ResponseEntity getAllLocksThisUserCanAccess() {
        try {
            Long id = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(userService.getAllLocksThisUserCanAccess(id), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint to get a {@code Lock}s the {@code User} can access.
     * @param id {@code @PathVariable} for the specific {@code Lock}.
     * @return {@code ResponseEntity}, OK if successful, BAD_REQUEST if it failed.
     */
    @GetMapping("/lock/{id}")
    public ResponseEntity getAllUsersThatCanAccessToThisLock(@PathVariable Long id){
        try {
            Long userId = UserPrinciple.getUserPrinciple().getId();
            return new ResponseEntity<>(userService.getAllUsersThatCanAccessToThisLock(id, userId), HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint to delete a {@code Lock}.
     * @param lockId the Id of the {@code Lock}.
     * @return {@code ResponseEntity}, OK if successful, BAD_REQUEST if it failed.
     */
    @DeleteMapping("/lock/{lockId}")
    public ResponseEntity leaveFromThisLock(@PathVariable Long lockId){
        try {
            Long userId = UserPrinciple.getUserPrinciple().getId();
            userService.leaveFromThisLock(lockId, userId);
            return new ResponseEntity<>("User left", HttpStatus.OK);
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
