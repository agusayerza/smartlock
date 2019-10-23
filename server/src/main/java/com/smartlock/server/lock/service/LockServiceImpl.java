package com.smartlock.server.lock.service;

import com.smartlock.server.lock.persistence.model.Lock;
import com.smartlock.server.lock.persistence.repository.LockRepository;
import com.smartlock.server.lock.presentation.dto.AddUserToLockDto;
import com.smartlock.server.lock.presentation.dto.CreateLockDto;
import com.smartlock.server.lock.presentation.dto.LockDto;
import com.smartlock.server.user.persistence.model.User;
import com.smartlock.server.user.persistence.repository.UserRepository;
import com.smartlock.server.user.presentation.dto.UserDto;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class LockServiceImpl implements LockService{

    private LockRepository lockRepository;
    private UserRepository userRepository;

    @Autowired
    public LockServiceImpl(UserRepository userRepository, LockRepository lockRepository) {
        this.userRepository = userRepository;
        this.lockRepository = lockRepository;
    }

    @Override
    public LockDto createLock(CreateLockDto lockDto, Long userAdminId) {
        User user = userRepository.getOne(userAdminId);
        Lock lock = new Lock(lockDto, userAdminId);
        lockRepository.save(lock);
        user.addNewLock(lock.getId());
        userRepository.save(user);
        return new LockDto(lock);
    }

    @Override
    public void deleteLock(Long id, Long userId) throws NotFoundException {
        User user = userRepository.getOne(userId);
        Optional<Lock> opLock = lockRepository.findById(id);
        if(opLock.isPresent()) {
            Lock lock = opLock.get();
            if (lock.getUserAdminId() == userId){
                List<UserDto> userDtos = getAllUsersThatCanAccessToThisLock(id, userId);
                for (UserDto userDto : userDtos) {
                    User userOfLock = userRepository.getOne(userDto.getId());
                    userOfLock.removeLock(lock.getId());
                }
                lock.setActive(false);
                lockRepository.save(lock);
                return;
            }
            throw new IllegalArgumentException("No es administrador de este candado");
        }
        throw new IllegalArgumentException("No existe candado con ese id");
    }

    @Override
    public LockDto getLock(Long id) throws NotFoundException {
        Optional<Lock> optionalLock = lockRepository.findById(id);
        if (optionalLock.isPresent()){
            Lock lock = optionalLock.get();
            if (lock.isActive()){
                return new LockDto(lock);
            }
        }
        throw new NotFoundException("Candado no encontrado");
    }

    @Override
    public List<UserDto> getAllUsersThatCanAccessToThisLock(Long lockId, Long userId) throws NotFoundException {
        Optional<Lock> optionalLock = lockRepository.findById(lockId);
        if (optionalLock.isPresent()){
            Lock lock = optionalLock.get();
            if (lock.isActive()){
                List<UserDto> userDtoList = new ArrayList<>();
                List<User> userList = userRepository.findAllByLocksIdContainingAndActiveTrue(lock.getId());
                for (User user : userList) {
                    userDtoList.add(new UserDto(user));
                }
                return userDtoList;
            }
        }
        throw new NotFoundException("Candado no encontrado");
    }

    @Override
    public void addUserToThisLock(AddUserToLockDto addUserToLockDto, Long userId) throws NotFoundException {
        Optional<Lock> optionalLock = lockRepository.findById(addUserToLockDto.getLockId());
        if (optionalLock.isPresent()) {
            Lock lock = optionalLock.get();
            if (lock.getUserAdminId() == userId) {
                if (lock.isActive()) {
                    Optional<User> optionalUser = userRepository.findById(addUserToLockDto.getUserId());
                    if(optionalUser.isPresent()){
                        User user = optionalUser.get();
                        if (user.isActive()){
                            user.addNewLock(lock.getId());
                            return;
                        }
                    }
                    throw new IllegalArgumentException("Usuario no encontrado");
                }
                throw new NotFoundException("Candado no encontrado");
            }
            throw new IllegalArgumentException("No puede agregar un usuario");
        }
        throw new NotFoundException("Candado no encontrado");
    }
}
