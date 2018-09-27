package com.aswin.locationtracker;

import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RealmDB {

    private static final String TAG = "aswin";
    private static RealmDB realmDB;
    private static Context context;
    private static Realm realm;

    public static RealmDB with(Context con) {
        realmDB = new RealmDB();
        context = con;
        realmDB.configureRealm();
        realm = Realm.getDefaultInstance();
        return realmDB;
    }

    public void configureRealm() {
        Realm.init(context);
        String path = Environment.getExternalStorageDirectory().getPath();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("aswin.realm")
                .deleteRealmIfMigrationNeeded()
                .directory(new File(path))
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public void setCurrentUser(String name) {
        final CurrentUser currentUser = new CurrentUser();
        currentUser.setUsername(name);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(currentUser);
            }
        });
    }

    public void deleteCurrentUser() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(CurrentUser.class);
            }
        });
    }

    public boolean updateUser(final UserModel userModel) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(userModel);
            }
        });

        return true;
    }

    public boolean isUserExist(String userName) {
        RealmResults<UserModel> realmResults = realm.where(UserModel.class).equalTo("username", userName).findAll();
        return realmResults.isEmpty();
    }

    public boolean isPasswordMatch(String userName, String pwd) {
        RealmResults<UserModel> realmResults = realm.where(UserModel.class).equalTo("username", userName)
                .equalTo("password", pwd)
                .findAll();
        return realmResults.isEmpty();
    }

    public UserModel getUserData(String username, String pwd) {

        return realm.where(UserModel.class).equalTo("username", username).findFirst();
    }

    public void updateLocation(List<Location> locationList) {
        final RealmList<String> lat = new RealmList<>();
        final RealmList<String> lon = new RealmList<>();

        if (getCurrentUser() != null) {
            UserModel userModel = realm.where(UserModel.class).equalTo("username", getCurrentUser()).findFirst();

            if (userModel != null) {
                lat.addAll(userModel.getLatitude());
                lon.addAll(userModel.getLongitude());
            } else {
                userModel = new UserModel("aswin", "qwerty", lat, lon);
            }
            for (Location l :
                    locationList) {
                lat.add(String.valueOf(l.getLatitude()));
                lon.add(String.valueOf(l.getLongitude()));
            }

            final UserModel finalUserModel = userModel;

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    finalUserModel.setLatitude(lat);
                    finalUserModel.setLongitude(lon);
                    Log.e(TAG, "execute: ");
                    realm.insertOrUpdate(finalUserModel);
                }
            });

        }

    }

    private String getCurrentUser() {
        CurrentUser currentUser = realm.where(CurrentUser.class).findFirst();
        if (currentUser != null && currentUser.getUsername() != null)
            return realm.where(CurrentUser.class).findFirst().getUsername();
        else
            return null;
    }
}
