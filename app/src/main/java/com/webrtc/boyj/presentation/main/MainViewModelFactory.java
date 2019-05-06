package com.webrtc.boyj.presentation.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.webrtc.boyj.data.source.UserRepository;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    private final UserRepository repository;

    MainViewModelFactory(@NonNull UserRepository repository) {
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(repository);
        } else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
