package com.example.datingapp.client.chat;

import com.example.datingapp.client.user.UserDto;

import java.util.List;

public class ChatDto {

    private Long chatId;
    private String chatName;
    List<UserDto> usersWithoutAuthenticatedUser;

    public ChatDto () {
    }

    public ChatDto(Long chatId, String chatName, List<UserDto> usersWithoutAuthenticatedUser) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.usersWithoutAuthenticatedUser = usersWithoutAuthenticatedUser;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public List<UserDto> getUsersWithoutAuthenticatedUser() {
        return usersWithoutAuthenticatedUser;
    }

    public void setUsersWithoutAuthenticatedUser(List<UserDto> usersWithoutAuthenticatedUser) {
        this.usersWithoutAuthenticatedUser = usersWithoutAuthenticatedUser;
    }
}
