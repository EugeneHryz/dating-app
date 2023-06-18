package com.example.datingapp.messaging.dto;

public abstract class AbstractChatMessage {

    protected Long chatId;
    protected MessageType type;

    public AbstractChatMessage() {
    }

    public AbstractChatMessage(Long chatId, MessageType type) {
        this.chatId = chatId;
        this.type = type;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
