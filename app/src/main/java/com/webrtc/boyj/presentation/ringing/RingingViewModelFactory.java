package com.webrtc.boyj.presentation.ringing;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class RingingViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    private final String tel;

    RingingViewModelFactory(@NonNull final String tel) {
        this.tel = tel;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RingingViewModel.class)) {
            return (T) new RingingViewModel(tel);
        } else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
