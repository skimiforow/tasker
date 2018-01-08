package com.isep.tasker.tasker.Domain;

import java.io.Serializable;

/**
 * Created by skimiforow on 10/12/2017.
 */

public class LocationPlace implements Serializable{

    private String name;
    private String address;
    public double latitude;
    public double longitude;

    public LocationPlace() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name;
    }
}
