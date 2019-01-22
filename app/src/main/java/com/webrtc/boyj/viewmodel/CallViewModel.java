package com.webrtc.boyj.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

public class CallViewModel extends BaseViewModel {
    private ObservableBoolean isCalling = new ObservableBoolean();
    private MutableLiveData<Boolean> _isActive = new MutableLiveData<>();

    private LiveData<Boolean> isActive = _isActive;

    public CallViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        // do nothing
    }

    public void onHangUp() {
        _isActive.setValue(false);
    }

    public ObservableBoolean getIsCalling() {
        return isCalling;
    }

    public LiveData<Boolean> getIsActive() {
        return isActive;
    }
}
