package com.example.datingapp.searchpeople.recyclerview;

import java.util.Objects;

public class UserItem {

    private Long id;
    private String name;
    private String distance;

    public UserItem() {
    }

    public UserItem(Long id, String name, String distance) {
        this.id = id;
        this.name = name;
        this.distance = distance;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserItem userItem = (UserItem) o;
        return Objects.equals(id, userItem.id)
                && Objects.equals(name, userItem.name)
                && Objects.equals(distance, userItem.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, distance);
    }
}
