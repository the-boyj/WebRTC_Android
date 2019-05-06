package com.webrtc.boyj.presentation.call;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.webrtc.boyj.api.boyjrtc.BoyjRTC;

public class CallViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    private final BoyjRTC boyjRTC;

    public CallViewModelFactory(@NonNull BoyjRTC boyjRTC) {
        this.boyjRTC = boyjRTC;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CallViewModel.class)) {
            return (T) new CallViewModel(boyjRTC);
        } else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
