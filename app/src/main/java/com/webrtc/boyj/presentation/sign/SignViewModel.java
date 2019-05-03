package com.webrtc.boyj.presentation.sign;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
