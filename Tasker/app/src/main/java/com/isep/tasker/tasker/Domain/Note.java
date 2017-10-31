package com.isep.tasker.tasker.Domain;

/**
 * Created by skimiforow on 29/10/2017.
 */

public class Note extends Tasker {
    private String title;
    private String description;

    public Note() {
    }

    public Note(Reminder reminder, User user, String title, String description) {
        super(reminder, user);
        this.title = title;
        this.description = description;
    }
}
