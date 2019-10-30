package com.smartlock.server;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication implements ApplicationRunner {

    public ServerApplication() {
    }

    /**
     * Main starting method and class for the application
     * @param args the arguments received by the app
     */
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    /**
     * Used by spring after starting the Spring Context
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {
    }
}
