package com.example.datingapp.client.chat;

import com.example.datingapp.messaging.dto.UserStatus;

public class UserStatusDto {

    private long userId;
    private UserStatus status;

    public UserStatusDto() {
    }

    public UserStatusDto(long userId, UserStatus status) {
        this.userId = userId;
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
