package com.smartlock.server.user.service;

import com.smartlock.server.lock.persistence.model.Lock;
import com.smartlock.server.lock.persistence.repository.LockRepository;
import com.smartlock.server.lock.presentation.dto.LockDto;
import com.smartlock.server.security.service.UserPrinciple;
import com.smartlock.server.user.persistence.model.User;
import com.smartlock.server.user.persistence.repository.UserRepository;
import com.smartlock.server.user.presentation.dto.CreateUserDto;
import com.smartlock.server.user.presentation.dto.UserDto;
import com.smartlock.server.user.presentation.dto.UserWithoutLocksDto;
import javassist.NotFoundException;
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

    /**
     * Class constructor
     * @param userRepository {@code UserRepository} instantiated class corresponding to the current Spring profile.
     * @param passwordEncoder {@code PasswordEncoder} instantiated class corresponding to the current Spring profile.
     * @param lockRepository {@code LockRepository} instantiated class corresponding to the current Spring profile.
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, LockRepository lockRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.lockRepository = lockRepository;
    }

    /**
     * Used to create a User
     * @param userData DTO containing the data for the user to be created.
     * @return {@code UserDTO} containing the info of the successfully created user.
     */
    @Override
    public UserDto createUser(CreateUserDto userData) {
        if (userRepository.existsByEmail(userData.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
        User newUser = new User(userData);
        userRepository.save(newUser);
        return new UserDto(newUser);
    }

    /**
     * Searches a user by his Id and returns it.
     * @param id corresponding to the user to be searched.
     * @return {@code UserDTO} containing the info of the found user.
     */
    @Override
    public UserDto getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent())throw new IllegalArgumentException("User not found");
        return new UserDto(optionalUser.get());
    }

    /**
     * Gives the Id for the user who requested this.
     * @return the user's Id.
     */
    @Override
    public Long getMyID() {
        return UserPrinciple.getUserPrinciple().getId();
    }

    /**
     * Get's all the {@code Lock}s added to the library of the given user, identified by his Id.
     * @param userId the user's Id
     * @return a list of all the locks this user has on his library.
     */
    @Override
    public List<LockDto> getAllLocksThisUserCanAccess(Long userId) {
        User user = userRepository.getOne(userId);
        List<LockDto> lockDtoList = new ArrayList<>();
        for (Long lockId : user.getLocksId()) {
            lockDtoList.add(new LockDto(lockRepository.getOne(lockId)));
        }
        return lockDtoList;
    }


    /**
     * For a specific {@code Lock} returns all the users who have permission to access it.
     * Only the admin can ask for this information.
     * @param lockId the Id for the lock
     * @param userId the user, presumably the lock owner, who requested the information.
     * @return a {@code List} of {@code UserWithoutLocksDto} who can access the given lock.
     * @throws NotFoundException when the Lock does not exist.
     */
    @Override
    public List<UserWithoutLocksDto> getAllUsersThatCanAccessToThisLock(Long lockId, Long userId) throws NotFoundException {
        Lock lock = getLockIfPresentAndActive(lockId);
        List<UserWithoutLocksDto> userDtoList = new ArrayList<>();
        List<User> userList = userRepository.findAllByLocksIdContaining(lock.getId());
        for (User user : userList) {
            userDtoList.add(new UserWithoutLocksDto(user));
        }
        return userDtoList;
    }

    /**
     * Removes the {@code User} that requested it from a {@code Lock}. Can NOT be performed by the Lock owner.
     * @param lockId the Id of the {@code Lock} that the user wants to remove from his library.
     * @param userId the Id for the user that performed the action.
     * @throws NotFoundException when the Lock does not exist.
     */
    @Override
    public void leaveFromThisLock(Long lockId, Long userId) throws NotFoundException {
        User user = userRepository.getOne(userId);
        if (!user.getLocksId().contains(lockId)) throw new NotFoundException("You cannot access that lock");
        user.removeLock(lockId);
        userRepository.save(user);
    }

    private Lock getLockIfPresentAndActive(Long lockId) throws NotFoundException {
        Optional<Lock> optionalLock = lockRepository.findById(lockId);
        if (!optionalLock.isPresent()) throw new NotFoundException("Lock not found");
        Lock lock = optionalLock.get();
        if (!lock.isActive()) throw new NotFoundException("Lock not found");
        return lock;
    }
}
