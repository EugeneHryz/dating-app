package com.example.datingapp.messaging.dto;

public class NewMessage extends AbstractChatMessage {

    private long messageId;
    private long timestamp;
    private long fromUserId;
    private String content;

    public NewMessage() {
    }

    public NewMessage(Long chatId, long messageId, long timestamp, long fromUserId, String content) {
        super(chatId, MessageType.NEW_MESSAGE);
        this.messageId = messageId;
        this.timestamp = timestamp;
        this.fromUserId = fromUserId;
        this.content = content;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
