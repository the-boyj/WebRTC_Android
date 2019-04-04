package com.webrtc.boyj.presentation.ringing;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.presentation.BaseViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class RingingViewModel extends BaseViewModel {
    @NonNull
    private final String tel;
    @NonNull
    private final BoyjRTC boyjRTC;
    @NonNull
    private final ObservableBoolean isKnockReceived = new ObservableBoolean();

    RingingViewModel(@NonNull final String tel) {
        this.tel = tel;
        this.boyjRTC = new BoyjRTC();

        addDisposable(boyjRTC.knock()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> isKnockReceived.set(true)));
    }

    void awaken(@NonNull final AwakenPayload payload) {
        boyjRTC.awaken(payload);
    }

    void reject() {
        boyjRTC.reject();
    }

    @NonNull
    public String getTel() {
        return tel;
    }

    @NonNull
    public ObservableBoolean getIsKnockReceived() {
        return isKnockReceived;
    }
}
