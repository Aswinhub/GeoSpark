package com.aswin.geospark.common;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {
    private static AppPreference appPreference;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private AppPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized AppPreference getInstance(Context context) {
        if (appPreference == null) {
            appPreference = new AppPreference(context);
        }
        return appPreference;
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);
        editor.apply();
    }

    public static void setBoolean(Context context, String key, boolean value) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(key, value);
        editor.apply();

    }

    public static void getString(Context context, String key) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);

        sharedPreferences.getString(key, "");

    }

    public static boolean getBoolean(Context context, String key) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(key, false);

    }

    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void setBoolean(String key, boolean value) {

        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getString(String key) {

        return sharedPreferences.getString(key, "");
    }

    public boolean getBoolean(String key) {

        return sharedPreferences.getBoolean(key, false);
    }

    public void deletePreference() {
        editor.clear().commit();
    }
}
