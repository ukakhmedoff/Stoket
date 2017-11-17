package ru.snatcher.stoket.data.repositories;

import android.app.Application;

import javax.inject.Inject;

import ru.snatcher.stoket.data.local.LocalPropertiesClient;

public class UserRepository {

    private final LocalPropertiesClient localPropertiesClient;

    @Inject
    public UserRepository(Application application) {
        localPropertiesClient = new LocalPropertiesClient(application);
    }

    public int getGender() {
        return localPropertiesClient.getGender();
    }

    public void setGender(int gender) {
        localPropertiesClient.storeUserGender(gender);
    }

    public boolean appStarted() {
        return localPropertiesClient.appStarted();
    }
}