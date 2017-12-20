package com.isep.tasker.tasker.Domain;

import java.io.Serializable;

/**
 * Created by skimiforow on 29/10/2017.
 */

public class Note extends Tasker implements Serializable {
    private String title;
    private String description;
    private String key;

    public Note() {
    }

    public Note(Reminder reminder, User user, String title, String description) {
        super(reminder, user);
        this.title = title;
        this.description = description;
        setKey ( title, description );

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

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public void setKey(String title, String description) {
        this.key = title + description;
    }

    public String getKey() {
        return key;
    }

}
