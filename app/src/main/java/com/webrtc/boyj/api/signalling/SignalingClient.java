package com.webrtc.boyj.api.signalling;


import android.support.annotation.NonNull;

import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.api.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.signalling.payload.SdpPayload;

import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

public class SignalingClient {

    @NonNull
    private static final SocketIOClient socketIOClient;

    static {
        socketIOClient = new SocketIOClient();
    }

    private CompletableSubject knockSubject = CompletableSubject.create();

    public SignalingClient() {
        socketIOClient.on(SignalingEventString.EVENT_KNOCK, args -> knockSubject.onComplete());
        socketIOClient.connect();
    }

    public void emitDial(@NonNull final DialPayload dialPayload) {
        socketIOClient.emit(SignalingEventString.EVENT_DIAL, dialPayload.toJson());
    }

    public void emitAwaken(@NonNull final AwakenPayload awakenPayload) {
        socketIOClient.emit(SignalingEventString.EVENT_AWAKEN, awakenPayload.toJson());
    }

    public void emitAccept() {
        socketIOClient.emit(SignalingEventString.EVENT_ACCEPT);
    }

    public void emitReject() {
        socketIOClient.emit(SignalingEventString.EVENT_REJECT);
    }

    public void emitSdp(@NonNull final SdpPayload sdpPayload) {
        socketIOClient.emit(SignalingEventString.EVENT_SEND_SDP, sdpPayload.toJson());
    }

    public void emitIceCandidate(@NonNull final IceCandidatePayload iceCandidatePayload) {
        socketIOClient.emit(SignalingEventString.EVENT_SEND_ICE, iceCandidatePayload.toJson());
    }

    public void emitBye() {
        socketIOClient.emit(SignalingEventString.EVENT_BYE);
    }

    public void disconnect() {
        socketIOClient.disconnect();
    }

    public CompletableSubject getKnockSubject() {
        return knockSubject;
    }


}
