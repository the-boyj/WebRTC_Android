package com.webrtc.boyj.presentation.ringing;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.presentation.BaseViewModel;

public class RingingViewModel extends BaseViewModel {
    @NonNull
    private final String tel;
    @NonNull
    private final BoyjRTC boyjRTC;

    RingingViewModel(@NonNull String tel) {
        this.tel = tel;
        this.boyjRTC = new BoyjRTC();
    }

    void reject() {
        boyjRTC.reject();
    }

    @NonNull
    public String getTel() {
        return tel;
    }
}
