package com.isep.tasker.tasker.Domain;

/**
 * Created by davidpinheiro on 06/01/2018.
 */

public class UserItem {
    private String id;
    private String email;

    public UserItem(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return this.email;
    }
}
