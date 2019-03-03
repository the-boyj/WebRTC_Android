package com.webrtc.boyj.presentation.call;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;

public class CallViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    private final User otherUser;

    CallViewModelFactory(@NonNull User otherUser) {
        this.otherUser = otherUser;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CallViewModel.class)) {
            return (T) new CallViewModel(otherUser);
        } else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
