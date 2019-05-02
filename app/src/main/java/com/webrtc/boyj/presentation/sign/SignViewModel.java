package com.webrtc.boyj.presentation.sign;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.presentation.BaseViewModel;

public class SignViewModel extends BaseViewModel {
    @NonNull
    private ObservableField<String> id = new ObservableField<>();
    @NonNull
    private MutableLiveData<String> signIn = new MutableLiveData<>();

    public void setId(@Nullable final String id) {
        if (id != null) {
            this.id.set(id);
        }
    }

    public void signInClicked() {
        final String id = this.id.get();
        if (id != null && !id.isEmpty()) {
            signIn.setValue(id);
        }
    }

    @NonNull
    public ObservableField<String> getId() {
        return id;
    }

    @NonNull
    public LiveData<String> getSignIn() {
        return signIn;
    }
}
