package com.example.datingapp.client.chat;

public class PrivateChatMessageDto {

    private long id;
    private long timestamp;
    private boolean isIncoming;
    private String content;

    public PrivateChatMessageDto() {
    }

    public PrivateChatMessageDto(long id, long timestamp, boolean isIncoming, String content) {
        this.id = id;
        this.timestamp = timestamp;
        this.isIncoming = isIncoming;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getIsIncoming() {
        return isIncoming;
    }

    public void setIsIncoming(boolean incoming) {
        isIncoming = incoming;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
