package com.smartlock.server.lock.service;

import com.smartlock.server.lock.persistence.model.Lock;
import com.smartlock.server.lock.persistence.repository.LockRepository;
import com.smartlock.server.lock.presentation.dto.CreateLockDto;
import com.smartlock.server.lock.presentation.dto.LockDto;
import com.smartlock.server.user.persistence.model.User;
import com.smartlock.server.user.persistence.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * Make the lock active if is found
     * @param lockDto DTO containing uuid of the lock and the future name of the lock
     * @param userId id of the user that made the request
     * @return {@code LockDTO} containing the info of the successfully claimed lock.
     * @throws NotFoundException
     */
    @Override
    public LockDto addLock(CreateLockDto lockDto, Long userId) throws NotFoundException {
        User user = userRepository.getOne(userId);
        Optional<Lock> opLock = lockRepository.findByUuid(lockDto.getUuid());
        if (opLock.isPresent()){
            Lock lock = opLock.get();
            if(lock.getName() == null){
                lock.setName(lockDto.getName());
            }
            lock.setActive(true);
            user.addNewLock(lock.getId());
            lockRepository.save(lock);
            userRepository.save(user);
            return new LockDto(lock);
        }
        throw new NotFoundException("Lock not found");
    }
//
//    /**
//     * Delete the lock, setting it's active parameter as false and removing it from all locks' list of the users
//     * @param id the Id for the lock
//     * @throws NotFoundException
//     */
//    @Override
//    public void deleteLock(Long id) throws NotFoundException {
//        Optional<Lock> opLock = lockRepository.findById(id);
//        if(opLock.isPresent()) {
//            Lock lock = opLock.get();
//                List<User> userList = userRepository.findAllByLocksIdContaining(lock.getId());
//                for (User user : userList) {
//                    user.removeLock(lock.getId());
//                    userRepository.save(user);
//                }
//                lock.setActive(false);
//                lockRepository.save(lock);
//                return;
//            }
//        throw new NotFoundException("Lock not found");
//    }

    /**
     * Searchs a lock by its Id and returns it
     * @param id corresponding to the lock to be searched.
     * @return{@code LockDTO} containing the info of the found lock.
     * @throws NotFoundException
     */
    @Override
    public LockDto getLock(Long id) throws NotFoundException {
        Optional<Lock> optionalLock = lockRepository.findById(id);
        if (optionalLock.isPresent()){
            Lock lock = optionalLock.get();
            if (lock.isActive()){
                return new LockDto(lock);
            }
        }
        throw new NotFoundException("Lock not found");
    }

    @Override
    public String getLockStatus(final String uuid) throws NotFoundException {
        // Remember this method is only called by the lock it self
        Lock lock = lockRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("ERR:1"));

        if(lock.isOpen()){
            return "OPEN";
        }
        return "CLOSE";
    }

    @Override
    public String getSetLockOpen(String uuid, boolean open, Long userId) throws NotFoundException {
        Lock lock = lockRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("ERR:1"));
        User user = userRepository.getOne(userId);
        if(!user.getLocksId().contains(lock.getId())) throw new IllegalArgumentException("You cannot access that lock");
        lock.setOpen(open);
        lockRepository.save(lock);
        // todo: maybe return a lockdto with the isOpen bool?
        return "";
    }

    @Override
    public void createRandomLock(String uuid) {
        Lock lock = new Lock();
        lock.setActive(false);
        lock.setUuid(uuid);
        lockRepository.save(lock);
    }
}
