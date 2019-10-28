package com.smartlock.server.userValidator.presentation.controller;

import com.smartlock.server.security.service.UserPrinciple;
import com.smartlock.server.userValidator.presentation.dto.UserLockValidatorDto;
import com.smartlock.server.userValidator.service.UserValidatorServiceImpl;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/usersValidator")
public class UserValidatorResource {

    @Autowired
    UserValidatorServiceImpl userValidatorService;

    /**
     * Put request executed to add a Lock to your library.
     * @param userLockValidatorDto DTO that contains the {@code Lock} desiered to be added as well as the code to
     *                             validate the invite.
     * @return {@code ResponseEntity}, OK if successful, BAD_REQUEST if the invitation was not found or the code was
     *          invalid.
     */
    @PutMapping()
    public ResponseEntity validateUserAndLock(@Valid @RequestBody UserLockValidatorDto userLockValidatorDto) {
        try {
            Long userId = UserPrinciple.getUserPrinciple().getId();
            userValidatorService.validateUserAndLock(userLockValidatorDto, userId);
            return new ResponseEntity<>("Code validated", HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}