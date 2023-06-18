package com.example.datingapp.client.chat;

public class ChatMessagesInfo {

    private long numberOfMessages;
    private long numberOfUnreadMessages;
    private Long ourMessageLastReadTime;
    private Long theirMessageLastReadTime;

    public ChatMessagesInfo() {
    }

    public ChatMessagesInfo(long numberOfMessages,
                            long numberOfUnreadMessages,
                            Long ourMessageLastReadTime,
                            Long theirMessageLastReadTime) {
        this.numberOfMessages = numberOfMessages;
        this.numberOfUnreadMessages = numberOfUnreadMessages;
        this.ourMessageLastReadTime = ourMessageLastReadTime;
        this.theirMessageLastReadTime = theirMessageLastReadTime;
    }

    public long getNumberOfMessages() {
        return numberOfMessages;
    }

    public void setNumberOfMessages(long numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    public long getNumberOfUnreadMessages() {
        return numberOfUnreadMessages;
    }

    public void setNumberOfUnreadMessages(long numberOfUnreadMessages) {
        this.numberOfUnreadMessages = numberOfUnreadMessages;
    }

    public Long getOurMessageLastReadTime() {
        return ourMessageLastReadTime;
    }

    public void setOurMessageLastReadTime(Long ourMessageLastReadTime) {
        this.ourMessageLastReadTime = ourMessageLastReadTime;
    }

    public Long getTheirMessageLastReadTime() {
        return theirMessageLastReadTime;
    }

    public void setTheirMessageLastReadTime(Long theirMessageLastReadTime) {
        this.theirMessageLastReadTime = theirMessageLastReadTime;
    }
}
