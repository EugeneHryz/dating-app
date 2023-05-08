package com.example.datingapp.geolocation;

public class LocationRequestDto {

    private String latitude;
    private String longitude;

    public LocationRequestDto() {
    }

    public LocationRequestDto(String latitude, String longitude) {
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
}
