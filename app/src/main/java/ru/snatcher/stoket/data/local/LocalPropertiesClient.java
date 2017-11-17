package ru.snatcher.stoket.data.local;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class LocalPropertiesClient {

    private static final String KEY_USER_GENDER = "KEY_USER_GENDER";

    private final SharedPreferences preferences;

    public LocalPropertiesClient(Application application) {
        preferences = application.getSharedPreferences("user_settings", Context.MODE_PRIVATE);
    }

    public void storeUserGender(int gender) {
        preferences.edit().putInt(KEY_USER_GENDER, gender).apply();
    }

    public int getGender() {
        return preferences.getInt(KEY_USER_GENDER, -1);
    }

    public boolean appStarted() {
        return preferences.contains(KEY_USER_GENDER);
    }
}