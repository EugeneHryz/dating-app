package com.example.datingapp.messaging.dto;

public class SendMessageDto {

    private long chatId;
    private long fromUserId;
    private long timestamp;
    private String content;

    public SendMessageDto() {
    }

    public SendMessageDto(Long chatId, long fromUserId,
                          long timestamp, String content) {
        this.chatId = chatId;
        this.fromUserId = fromUserId;
        this.timestamp = timestamp;
        this.content = content;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SendMessageDto{" +
                "chatId=" + chatId +
                ", fromUserId=" + fromUserId +
                ", timestamp=" + timestamp +
                ", content='" + content + '\'' +
                '}';
    }
}
