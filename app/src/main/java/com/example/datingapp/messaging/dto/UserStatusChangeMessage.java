package com.example.datingapp.messaging.dto;

public class UserStatusChangeMessage extends AbstractChatMessage {

    private Long userId;
    private UserStatus userStatus;

    public UserStatusChangeMessage() {
    }

    public UserStatusChangeMessage(Long chatId, Long userId, UserStatus userStatus) {
        super(chatId, MessageType.USER_STATUS_CHANGE);
        this.userId = userId;
        this.userStatus = userStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}
