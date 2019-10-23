package com.smartlock.server.user.service;

import com.smartlock.server.lock.persistence.repository.LockRepository;
import com.smartlock.server.lock.presentation.dto.LockDto;
import com.smartlock.server.security.service.UserPrinciple;
import com.smartlock.server.user.persistence.model.User;
import com.smartlock.server.user.persistence.repository.UserRepository;
import com.smartlock.server.user.presentation.dto.CreateUserDto;
import com.smartlock.server.user.presentation.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private LockRepository lockRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, LockRepository lockRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.lockRepository = lockRepository;
    }

    @Override
    public UserDto createUser(CreateUserDto userData) {
        if(userRepository.existsByEmail(userData.getEmail())){
            throw new IllegalArgumentException("El email esta en uso");
        }
        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
        User newUser = new User(userData);
        userRepository.save(newUser);
        return new UserDto(newUser);
    }

//    @Override
//    public void deleteUser(Long id) {
//        Optional<User> optionalUser = userRepository.findById(id);
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            user.setActive(false);
//            userRepository.save(user);
//            return;
//        }
//        throw new IllegalArgumentException("No existe usuario con ese id");
//    }

    @Override
    public UserDto getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new UserDto(user);
        }
        throw new IllegalArgumentException("No existe usuario con ese id");    }

    @Override
    public Long getMyID() {
        return UserPrinciple.getUserPrinciple().getId();
    }

    @Override
    public List<LockDto> getAllLocksThisUserCanAccess(Long userId) {
        User user = userRepository.getOne(userId);
        List<LockDto> lockDtoList = new ArrayList<>();
        for (Long lockId : user.getLocksId()) {
            lockDtoList.add(new LockDto(lockRepository.getOne(lockId)));
        }
        return lockDtoList;
    }

}
