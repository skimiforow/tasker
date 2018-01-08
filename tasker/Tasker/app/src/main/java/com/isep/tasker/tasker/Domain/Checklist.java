package com.isep.tasker.tasker.Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skimiforow on 29/10/2017.
 */

public class Checklist extends Tasker {

    List<Items> itemsList;
    int index;

    public Checklist() {
    }

    public Checklist(Reminder reminder, User user) {
        super(reminder, user);
        this.itemsList = new ArrayList<>();
        index = 0;

    }

    public boolean addItem(String text){
        Items items = new Items (  );
        items.setDescription ( text );
        itemsList.add ( index, items);
        index++;
        return true;
    }
}
