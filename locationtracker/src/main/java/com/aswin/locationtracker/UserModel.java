package com.aswin.locationtracker;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserModel extends RealmObject {

    @PrimaryKey
    private String username;
    private String password;
    private RealmList<String > latitude;
    private RealmList<String > longitude;

    public UserModel() {
    }

    public UserModel(String username, String password, RealmList<String> latitude, RealmList<String> longitude) {
        this.username = username;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RealmList<String> getLatitude() {
        return latitude;
    }

    public void setLatitude(RealmList<String> latitude) {
        this.latitude = latitude;
    }

    public RealmList<String> getLongitude() {
        return longitude;
    }

    public void setLongitude(RealmList<String> longitude) {
        this.longitude = longitude;
    }
}
