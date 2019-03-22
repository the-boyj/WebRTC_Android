package com.webrtc.boyj.presentation.ringing;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.api.signalling.SignalingClient;

public class RingingViewModel {

    @NonNull
    private final BoyjRTC boyjRTC;

    public RingingViewModel() {
        boyjRTC = new BoyjRTC();
    }

    public void acceptAction() {
        boyjRTC.acceptAction();
    }

    public void rejectAction() {
        boyjRTC.rejectAction();
    }
}
