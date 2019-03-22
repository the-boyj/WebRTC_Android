package com.webrtc.boyj.api;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.api.signalling.payload.FCMPayload;
import com.webrtc.boyj.presentation.ringing.RingingActivity;
import com.webrtc.boyj.utils.App;

public class BoyjRTC {
    @NonNull
    private final static SignalingClient signalingClient;

    static {
        signalingClient = new SignalingClient();
    }

    public BoyjRTC() {
    }

    public void onPushReceived(@NonNull final FCMPayload fcmPayload) {

        //노크 이벤트를 수신하면 , 링액티비티를 실행시킨다. 이 서브스크립션은 1번만 실행된후 해제되므로 따로 리소스 해제를 시킬 필요가 없다.
        signalingClient.getKnockSubject().take(1).subscribe(o -> {
            Intent intent = RingingActivity.getLaunchIntent(App.getContext(), fcmPayload);
            App.getContext().startActivity(intent);
        });


        final String room = fcmPayload.getRoom();
        final AwakenPayload awakenPayload = new AwakenPayload.Builder(room).build();
        signalingClient.emitAwaken(awakenPayload);

    }

    //앱 유저로 부터 온 이벤트 처리
    public void callAction(@NonNull final DialPayload dialPayload) {
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
        signalingClient.disconnect();
    }
}
