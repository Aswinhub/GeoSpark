package com.aswin.locationtracker;

import io.realm.RealmObject;

public class CurrentUser extends RealmObject {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
