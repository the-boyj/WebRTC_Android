package com.webrtc.boyj.api;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.api.signalling.payload.FCMPayload;
import com.webrtc.boyj.data.model.User;

public class BoyjRTC {
    @NonNull
    private final static SignalingClient signalingClient;


    static {
        signalingClient = new SignalingClient();
    }

    public BoyjRTC() {
    }

    public void pushReceived(@NonNull final FCMPayload fcmPayload) {
        final String room = fcmPayload.getRoom();

        final AwakenPayload awakenPayload = new AwakenPayload.Builder(room).build();
        signalingClient.emitAwaken(awakenPayload);
    }


    //앱 유저로 부터 온 이벤트 처리
    public void callActionTo(@NonNull final User user) {
        final DialPayload dialPayload = new DialPayload.Builder(user.getDeviceToken()).build();
        signalingClient.emitDial(dialPayload);
    }

    public void acceptAction() {
        signalingClient.emitAccept();
    }

    public void rejectAction() {
        signalingClient.emitReject();
    }

    public void hangupAction() {
        signalingClient.emitBye();
    }
}
