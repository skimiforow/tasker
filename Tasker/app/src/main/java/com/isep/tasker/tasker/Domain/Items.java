package com.isep.tasker.tasker.Domain;

/**
 * Created by skimiforow on 29/10/2017.
 */

class Items {
    private String description;
    private State state;


    public Items() {
        this.description = "";
        this.state = State.Created;
    }

    public Items(String description) {
        this.description = description;
        this.state = State.Created;
    }
}
