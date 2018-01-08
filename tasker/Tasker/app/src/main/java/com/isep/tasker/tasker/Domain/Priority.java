package com.isep.tasker.tasker.Domain;

/**
 * Created by skimiforow on 26/11/2017.
 */

public enum Priority {
    LOW("Low priority"),
    NORMAL("Normal priority"),
    HIGH("High priority");

    private String friendlyName;

    private Priority(String friendlyName){
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return  friendlyName;
    }
}


