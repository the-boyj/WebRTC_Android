package com.webrtc.boyj.presentation.call;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.webrtc.boyj.api.BoyjRTC;

public class CallViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    private final BoyjRTC boyjRTC;

    CallViewModelFactory(@NonNull final BoyjRTC boyjRTC) {
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
