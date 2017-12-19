package com.isep.tasker.tasker.Domain;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by skimiforow on 10/12/2017.
 */

public class LocationPlace {

    private String name;
    private String address;
    private LatLng coordinates;

    public LocationPlace() {
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
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
