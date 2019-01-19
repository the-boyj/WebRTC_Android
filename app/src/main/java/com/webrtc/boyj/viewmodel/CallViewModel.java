package com.webrtc.boyj.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

public class CallViewModel extends BaseViewModel {
    private ObservableBoolean isCalling = new ObservableBoolean();
    private MutableLiveData<Boolean> isActive = new MutableLiveData<>();

    public CallViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        // do nothing
    }

    public void onHangUp() {
        isActive.setValue(false);
    }

    public ObservableBoolean getIsCalling() {
        return isCalling;
    }

    public MutableLiveData<Boolean> getIsActive() {
        return isActive;
    }
}
