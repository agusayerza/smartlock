package com.smartlock.server;

import com.smartlock.server.lock.presentation.dto.CreateLockDto;
import com.smartlock.server.lock.service.LockService;
import com.smartlock.server.user.presentation.dto.CreateUserDto;
import com.smartlock.server.user.presentation.dto.UserDto;
import com.smartlock.server.user.service.UserService;
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
    private UserService userService;
    private LockService lockService;
    private boolean createdData = false;

    /**
     * {@code DemoRunner} constructor. The {@code DemoRunner} is a class used to instantiate and load into the
     * database the objects to be used during demos.
     * @param userService {@code UserService} instantiated class corresponding to the current Spring profile.
     * @param lockService {@code LockService} instantiated class corresponding to the current Spring profile.
     * @param env {@code Environment} Spring Environment instantiated, corresponding to the current Spring profile, and
     *                               used to determine on runtime if the demo data has to be created or not.
     */
    @Autowired
    public DemoRunner(UserService userService, LockService lockService, Environment env) {
        this.userService = userService;
        this.lockService = lockService;
        this.env = env;
    }

    /**
     * Callback used to run the bean.
     * @param args incoming main method arguments
     */
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



        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setEmail("a@a.a");
        createUserDto.setPassword("password");
        UserDto userDto = this.userService.createUser(createUserDto);
        logger.info("User created with id: " + userDto.getId());


        ArrayList<String> uidList = generateListOfUuid(5);
        for (int i = 0; i < uidList.size(); i++) {
            lockService.createRandomLock(uidList.get(i));
            logger.info("Creating lock " + uidList.get(i));
        }

        CreateLockDto createLockDto = new CreateLockDto();
        createLockDto.setName("Test Lock");
        createLockDto.setUuid("18bfd86f-539e-40e2-a917-64c9ed1d42d9");
//        try {
//            lockService.addLock(createLockDto, userDto.getId());
//        } catch (NotFoundException e) {
//            logger.error("Error creating test lock");
//        }

        createdData = true;
    }

    private ArrayList<String> generateListOfUuid(int number) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            result.add(generateUuid());
        }
        result.add("18bfd86f-539e-40e2-a917-64c9ed1d42d9"); // demo lock
        return result;
    }

    private String generateUuid() {
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

