package com.example.datingapp.messaging.dto;

public class MessageRead extends AbstractChatMessage {

    private long lastReadMessageTime;

    public MessageRead() {
    }

    public MessageRead(Long chatId, long lastReadMessageTime) {
        super(chatId, MessageType.MESSAGE_READ);
        this.lastReadMessageTime = lastReadMessageTime;
    }

    public long getLastReadMessageTime() {
        return lastReadMessageTime;
    }

    public void setLastReadMessageTime(long lastReadMessageTime) {
        this.lastReadMessageTime = lastReadMessageTime;
    }
}
