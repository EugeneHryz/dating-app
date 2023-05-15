package com.example.datingapp.client.user;

public class UserDto {

    private Long id;
    private String username;
    private Long distance;

    public UserDto() {
    }

    public UserDto(Long id, String username, Long distance) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }
}
