package com.isep.tasker.tasker.Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skimiforow on 29/10/2017.
 */

class Tasker {
    private Reminder reminder;
    private List<User> userList;
    private State state;
    private Importance importance;

    public Tasker() {
        this.userList = new ArrayList<>();
        this.state = State.Created;
        this.importance = Importance.NormalPriority;
    }

    public Tasker(Reminder reminder, User user) {
        this.userList = new ArrayList<>();
        this.reminder = reminder;
        this.userList.add(user) ;
        this.state = State.Created;
        this.importance = Importance.NormalPriority;
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

    public Importance getImportance() {
        return importance;
    }

    public void setImportance(Importance importance) {
        this.importance = importance;
    }
}
