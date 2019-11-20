package com.smartlock.server.user.service;

import com.smartlock.server.lock.presentation.dto.LockDto;
import com.smartlock.server.user.presentation.dto.CreateUserDto;
import com.smartlock.server.user.presentation.dto.UserDto;
import com.smartlock.server.user.presentation.dto.UserWithoutLocksDto;
import javassist.NotFoundException;

import java.util.List;

public interface UserService {

    /**
     * Used to create a User
     * @param userData DTO containing the data for the user to be created.
     * UserDTO containing the info of the successfully created user.
     */
    UserDto createUser(CreateUserDto userData);

    /**
     * Searches a user by his Id and returns it.
     * @param id corresponding to the user to be searched.
     * UserDTO containing the info of the found user.
     */
    UserDto getUser(Long id);

    /**
     * Gives the Id for the user who requested this.
     * @return the user's Id.
     */
    Long getMyID();

    /**
     * For a specific Lock returns all the users who have permission to access it.
     * Only the admin can ask for this information.
     * @param lockId the Id for the lock
     * @param userId the user, presumably the lock owner, who requested the information.
     * @return a List of UserWithoutLocksDto who can access the given lock.
     * @throws NotFoundException when the Lock does not exist.
     * @see UserWithoutLocksDto
     */
    List<UserWithoutLocksDto> getAllUsersThatCanAccessToThisLock(Long lockId, Long userId) throws NotFoundException;

    /**
     * Removes the User that requested it from a Lock. Can NOT be performed by the Lock owner.
     * @param lockId the Id of the Lock that the user wants to remove from his library.
     * @param userId the Id for the user that performed the action.
     * @throws NotFoundException when the Lock does not exist.
     */
    void leaveFromThisLock(Long lockId, Long userId) throws NotFoundException;

    /**
     * Get's all the Locks added to the library of the given user, identified by his Id.
     * @param userId the user's Id
     * @return a list of all the locks this user has on his library.
     */
    List<LockDto> getAllLocksThisUserCanAccess(Long userId);

}
