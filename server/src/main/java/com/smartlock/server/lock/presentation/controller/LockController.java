package com.smartlock.server.lock.presentation.controller;


import com.smartlock.server.lock.service.LockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock")
public class LockController {
    private static Logger logger = LoggerFactory.getLogger(LockController.class);

    private LockService lockService;

    public LockController(@Autowired LockService lockService) {
        this.lockService = lockService;
    }


    @PostMapping
    private String ping(String ping){
        logger.info(ping);
        return "pong";
    }

    @GetMapping
    private String getMe(){
        logger.info("get");
        return "pong";
    }

}
