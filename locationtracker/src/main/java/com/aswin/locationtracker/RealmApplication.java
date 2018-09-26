package com.aswin.locationtracker;

import android.app.Application;
import android.os.Environment;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        Realm.init(getApplicationContext());
        String path = Environment.getExternalStorageDirectory().getPath();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("aswin.realm")
                .directory(new File(path))
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);

    }

}
