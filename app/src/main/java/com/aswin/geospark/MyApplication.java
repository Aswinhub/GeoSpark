package com.aswin.geospark;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.aswin.locationtracker.RealmApplication;


public class MyApplication extends Application {

    public static final String CHANNEL_ID = "mychannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotification();
    }

    private void createNotification() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

    }
}
