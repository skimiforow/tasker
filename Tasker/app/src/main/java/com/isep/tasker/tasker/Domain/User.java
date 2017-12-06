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
    private boolean isEmailVerified;

    public User(String name, String email, Uri photoUrl, boolean emailVerified) {
        this.taskerList = new ArrayList<>();
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.taskerList = new ArrayList<>();
        this.isEmailVerified = emailVerified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<Tasker> getTaskerList() {
        return taskerList;
    }

    public void setTaskerList(List<Tasker> taskerList) {
        this.taskerList = taskerList;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }
}
