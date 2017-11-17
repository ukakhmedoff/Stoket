package ru.snatcher.stoket.ui.activity.main;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import ru.snatcher.stoket.data.repositories.UserRepository;

public class MainActivityViewModel extends ViewModel {

    private final UserRepository userRepository;

    @Inject
    public MainActivityViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    boolean appStarted() {
        return userRepository.appStarted();
    }
}
