package com.example.datingapp.home;

import com.example.datingapp.messaging.dto.UserStatus;

import java.io.Serializable;

public class ChatItem implements Serializable {

    private Long id;
    private String name;
    private UserStatus status;

    public ChatItem() {
    }

    public ChatItem(Long id, String name, UserStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

}
