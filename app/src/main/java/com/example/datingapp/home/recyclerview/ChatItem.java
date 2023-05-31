package com.example.datingapp.home.recyclerview;

import com.example.datingapp.searchpeople.recyclerview.UserItem;

import java.util.Objects;

public class ChatItem {

    private Long id;
    private String name;
    private UserItem user;

    public ChatItem() {
    }

    public ChatItem(Long id, String name, String distance, UserItem user) {
        this.id = id;
        this.name = name;
        this.user = user;
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

    public UserItem getUser() {
        return user;
    }

    public void setUser(UserItem user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatItem chatItem = (ChatItem) o;
        return Objects.equals(id, chatItem.id)
                && Objects.equals(name, chatItem.name)
                && Objects.equals(user, chatItem.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, user);
    }
}
