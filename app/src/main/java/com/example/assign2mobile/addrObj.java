package com.example.assign2mobile;

public class addrObj { //this model object is used for store address data for the program.
    private int id;
    private String address;
    private Double latitude;
    private Double longitude;

    public addrObj(int id, String address, Double latitude, Double longitude){ //long constructor that includes id
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public addrObj(String address,Double latitude, Double longitude){ //constructor for when there is no id yet (before insertion into database)
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    } //basic getter setter methods.

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
