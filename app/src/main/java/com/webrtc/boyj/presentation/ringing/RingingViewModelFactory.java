package com.webrtc.boyj.presentation.ringing;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.webrtc.boyj.api.BoyjRTC;

public class RingingViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    private final BoyjRTC boyjRTC;

    RingingViewModelFactory(@NonNull final BoyjRTC boyjRTC) {
        this.boyjRTC = boyjRTC;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RingingViewModel.class)) {
            return (T) new RingingViewModel(boyjRTC);
        } else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
