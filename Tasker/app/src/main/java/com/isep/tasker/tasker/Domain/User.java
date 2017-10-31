package com.isep.tasker.tasker.Domain;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skimiforow on 29/10/2017.
 */

public class User {

    private String name;
    private String email;
    private Uri photoUrl;
    private List<Tasker> taskerList;

    public User() {
        this.taskerList = new ArrayList<>();
    }

    public User(String name, String email, Uri photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.taskerList = new ArrayList<>();
    }
}
