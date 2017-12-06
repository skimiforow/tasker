package com.isep.tasker.tasker.Domain;

/**
 * Created by skimiforow on 29/10/2017.
 */

public class Items {
    private String description;
    private State state;
    private Priority priority;


    public Items() {
        this.description = "";
        this.state = State.Created;
        this.priority = Priority.NORMAL;
    }

    public Items(String description) {
        this.description = description;
        this.state = State.Created;
        this.priority = Priority.NORMAL;
    }

    public String getDescription() {
        return description;
    }

    public State getState() {
        return state;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        if(this.state==state.Archived){
            return true;
        } else {
            return false;
        }
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
