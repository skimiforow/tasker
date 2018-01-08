package com.isep.tasker.tasker.Domain;

/**
 * Created by skimiforow on 29/10/2017.
 */

public class ReminderItem extends Tasker{
    private String title;
    private String smallDescription;

    public ReminderItem(User user) {
        super(new Reminder(),user);
        this.title = "";
        this.smallDescription = "";
    }

    public ReminderItem(Reminder reminder, User user, String title, String smallDescription) {
        super(reminder, user);
        this.title = title;
        this.smallDescription = smallDescription;
    }
}
