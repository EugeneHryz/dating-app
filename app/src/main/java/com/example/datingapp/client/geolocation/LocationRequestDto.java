package com.example.datingapp.client.geolocation;

import java.time.Instant;

public class LocationRequestDto {

    private String latitude;
    private String longitude;
    private Instant timestamp;

    public LocationRequestDto() {
    }

    public LocationRequestDto(String latitude, String longitude, Instant timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
