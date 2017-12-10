package com.isep.tasker.tasker.Domain;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by skimiforow on 29/10/2017.
 */

public class Reminder extends Tasker {
    private String title;
    private String description;
    private Date date;
    private Time time;
    private String key;
    private ArrayList<LocationPlace> listLocations;

    public Reminder() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public ArrayList<LocationPlace> getListLocations() {
        return listLocations;
    }

    public void setListLocations(ArrayList<LocationPlace> listLocations) {
        this.listLocations = listLocations;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String title, String description) {
        this.key = title + description;
    }
}
