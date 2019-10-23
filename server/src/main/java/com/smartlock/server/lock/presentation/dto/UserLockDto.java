package com.smartlock.server.lock.presentation.dto;

public class UserLockDto {
    private Long lockId;
    private Long userId;


    public Long getLockId() {
        return lockId;
    }

    public void setLockId(Long lockId) {
        this.lockId = lockId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
