package com.smartlock.server.user.presentation.controller;

import com.smartlock.server.security.service.UserPrinciple;
import com.smartlock.server.user.presentation.dto.CreateUserDto;
import com.smartlock.server.user.service.UserService;
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

//quien puede eliminar users? solo a si mismo
//    @DeleteMapping()
//    public ResponseEntity deleteUser() {
//        try {
//            Long id = UserPrinciple.getUserPrinciple().getId();
//            userService.deleteUser(id);
//            return new ResponseEntity<>("Usuario eliminado", HttpStatus.OK);
//        } catch (IllegalArgumentException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

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

}
