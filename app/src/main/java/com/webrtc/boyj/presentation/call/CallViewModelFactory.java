package com.webrtc.boyj.presentation.call;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class CallViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    private final String tel;

    CallViewModelFactory(@NonNull final String tel) {
        this.tel = tel;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CallViewModel.class)) {
            return (T) new CallViewModel(tel);
        } else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
