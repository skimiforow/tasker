package com.isep.tasker.tasker.Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skimiforow on 29/10/2017.
 */

public class Checklist extends Tasker {

    List<Items> itemsList;

    public Checklist() {
    }

    public Checklist(Reminder reminder, User user) {
        super(reminder, user);
        this.itemsList = new ArrayList<>();
        this.itemsList.add(new Items());

    }
}
