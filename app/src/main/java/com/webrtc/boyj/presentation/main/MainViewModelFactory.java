package com.webrtc.boyj.presentation.main;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.webrtc.boyj.data.repository.UserRepository;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    private final UserRepository repository;

    MainViewModelFactory(@NonNull UserRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            //noinspection unchecked
            return (T) new MainViewModel(repository);
        } else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
