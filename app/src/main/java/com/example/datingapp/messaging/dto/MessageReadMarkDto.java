package com.example.datingapp.messaging.dto;

public class MessageReadMarkDto {

    private Long chatId;
    private Long fromUserId;
    private Long lastReadMessageId;

    public MessageReadMarkDto() {
    }

    public MessageReadMarkDto(Long chatId, Long fromUserId, Long lastReadMessageId) {
        this.chatId = chatId;
        this.fromUserId = fromUserId;
        this.lastReadMessageId = lastReadMessageId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getLastReadMessageId() {
        return lastReadMessageId;
    }

    public void setLastReadMessageId(Long lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }
}
