package com.isep.tasker.tasker.Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skimiforow on 29/10/2017.
 */

public class Tasker {
    private Reminder reminder;
    private List<User> userList;
    private State state;
    private Priority priority;
    private String title;
    private String description;

    public Tasker() {
        this.userList = new ArrayList<>();
        this.state = State.Created;
    }

    public Tasker(Reminder reminder, User user) {
        this.userList = new ArrayList<>();
        this.reminder = reminder;
        this.userList.add(user) ;
        this.state = State.Created;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority importance) {
        this.priority = importance;
    }

    public void setStringState(String stringState) {
        this.state = State.valueOf ( stringState );
    }

    public void setStringPriority(String stringPriority) {
        this.priority = Priority.valueOf ( stringPriority );
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
}
