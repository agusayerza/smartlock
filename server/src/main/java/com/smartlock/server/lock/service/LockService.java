package com.smartlock.server.lock.service;

import com.smartlock.server.lock.presentation.dto.CreateLockDto;
import com.smartlock.server.lock.presentation.dto.LockDto;
import javassist.NotFoundException;

public interface LockService {


    /**
     * Make the lock active if is found and set requested user as lock's admin
     * @param lockDto DTO containing uuid of the lock and the future name of the lock
     * @param userAdminId id of the user that made the request
     * @return {@code LockDTO} containing the info of the successfully claimed lock.
     * @throws NotFoundException
     */
    LockDto addLock(CreateLockDto lockDto, Long userAdminId) throws NotFoundException;


    /**
     * Delete the lock, setting it's active parameter as false and removing it from all locks' list of the users
     * Only lock's admin can make this request
     * @param id the Id for the lock
     * @param userId the user, presumably the lock owner, who requested the information.
     * @throws NotFoundException
     */
    void deleteLock(Long id, Long userId) throws NotFoundException;


    /**
     * Searchs a lock by its Id and returns it
     * @param id corresponding to the lock to be searched.
     * @return{@code LockDTO} containing the info of the found lock.
     * @throws NotFoundException
     */
    LockDto getLock(Long id) throws NotFoundException;

    String getLockStatus(String uuid) throws NotFoundException;

    String getSetLockOpen(String uuid, boolean open) throws NotFoundException;

    void createLockWithInvalidAdmin(String uuid);
}
