package ru.snatcher.stoket.ui.fragments.intro;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import ru.snatcher.stoket.data.repositories.UserRepository;

public class IntroFragmentViewModel extends ViewModel {
    private final UserRepository repository;

    @Inject
    IntroFragmentViewModel(UserRepository repository) {
        this.repository = repository;
    }

    void setGender(int gender) {
        repository.setGender(gender);
    }
}