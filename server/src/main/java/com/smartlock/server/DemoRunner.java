package com.smartlock.server;

import com.smartlock.server.lock.presentation.dto.CreateLockDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Random;

@Component("DemoRunner")
@Transactional
public class DemoRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DemoRunner.class);

    private final Environment env;
    private boolean createdData = false;

    @Autowired
    public DemoRunner(Environment env) {
        this.env = env;
    }

    @Override
    public void run(String... args) {
        for (String profile: env.getActiveProfiles()) {
            if(profile.equalsIgnoreCase("local")){
                createDemoData();
                return;
            }
        }
        logger.info("Profile: \"local\" not found. Didn't create demo data");
    }

    private void createDemoData() {
        if(createdData) {
            logger.warn("Creating data again?");
            return;
        }

        logger.info("Creating demo data ...");

        ArrayList<String> uidList = generateListOfUid(5);
        for (int i = 0; i < 5; i++) {
            CreateLockDto createLockDto = new CreateLockDto();
            createLockDto.setUid(uidList.get(i));
            logger.info("Creating lock " + createLockDto.getUid());
        }
        createdData = true;
    }

    private ArrayList<String> generateListOfUid(int number) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            result.add(generateUid());
        }
        return result;
    }

    private String generateUid() {
        Random rand = new Random();
        String result = "";
        for (int i = 0; i < 36; i++) {
            if (i == 8 || i == 13 || i == 18 || i ==23){
                result = result.concat("-");
            } else {
                int randomNum = rand.nextInt(16);
                result = result.concat(Integer.toHexString(randomNum));
            }
        }
        return result;
    }
}

