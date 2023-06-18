package com.example.datingapp.client.chat;

public class ChatDto {

    private Long id;
    private String name;

    private Long numberOfUnreadMessages;
    private UserStatusDto userStatus;

    public ChatDto() {
    }

    public ChatDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ChatDto(Long id, String name, Long numberOfUnreadMessages, UserStatusDto userStatus) {
        this.id = id;
        this.name = name;
        this.numberOfUnreadMessages = numberOfUnreadMessages;
        this.userStatus = userStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumberOfUnreadMessages() {
        return numberOfUnreadMessages;
    }

    public void setNumberOfUnreadMessages(Long numberOfUnreadMessages) {
        this.numberOfUnreadMessages = numberOfUnreadMessages;
    }

    public UserStatusDto getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatusDto userStatus) {
        this.userStatus = userStatus;
    }
}
