package com.webrtc.boyj.api.signalling;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.api.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.signalling.payload.SdpPayload;
import com.webrtc.boyj.utils.Logger;

import org.json.JSONObject;
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

    @NonNull
    private CompletableSubject knockSubject = CompletableSubject.create();
    @NonNull
    private CompletableSubject readySubject = CompletableSubject.create();
    @NonNull
    private CompletableSubject byeSubject = CompletableSubject.create();
    
    @NonNull
    private PublishSubject<IceCandidate> iceCandidateSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<SessionDescription> sdpSubject = PublishSubject.create();


    public SignalingClient() {

        socketIOClient.on(SignalingEventString.EVENT_KNOCK, args -> knockSubject.onComplete());
        socketIOClient.on(SignalingEventString.EVENT_READY, args -> readySubject.onComplete());

        socketIOClient.on(SignalingEventString.EVENT_RECEIVE_SDP, args -> {
            SdpPayload payload = SdpPayload.fromJsonObject((JSONObject) args[0]);
            sdpSubject.onNext(payload.getSdp());
        });
        socketIOClient.on(SignalingEventString.EVENT_RECEIVE_ICE, args -> {
            IceCandidatePayload payload = IceCandidatePayload.fromJsonObject((JSONObject) args[0]);
            iceCandidateSubject.onNext(payload.getIceCandidate());
        });
        socketIOClient.connect();
    }

    public void emitDial(@NonNull final DialPayload dialPayload) {
        Logger.d("emitDial()");
        socketIOClient.emit(SignalingEventString.EVENT_DIAL, dialPayload.toJsonObject());
    }

    public void emitAwaken(@NonNull final AwakenPayload awakenPayload) {
        Logger.d("emitAwaken()");
        socketIOClient.emit(SignalingEventString.EVENT_AWAKEN, awakenPayload.toJsonObject());
    }

    public void emitAccept() {
        socketIOClient.emit(SignalingEventString.EVENT_ACCEPT);
    }

    public void emitReject() {
        socketIOClient.emit(SignalingEventString.EVENT_REJECT);
    }

    public void emitSdp(@NonNull final SdpPayload sdpPayload) {
        socketIOClient.emit(SignalingEventString.EVENT_SEND_SDP, sdpPayload.toJsonObject());
    }

    public void emitIceCandidate(@NonNull final IceCandidatePayload iceCandidatePayload) {
        socketIOClient.emit(SignalingEventString.EVENT_SEND_ICE, iceCandidatePayload.toJsonObject());
    }

    public void emitBye() {
        socketIOClient.emit(SignalingEventString.EVENT_BYE);
    }

    public void disconnect() {
        socketIOClient.disconnect();
    }

    @NonNull
    public CompletableSubject getKnockSubject() {
        return knockSubject;
    }

    @NonNull
    public CompletableSubject getReadySubject() {
        return readySubject;
    }

    @NonNull
    public CompletableSubject getByeSubject() {
        return byeSubject;
    }

    @NonNull
    public PublishSubject<IceCandidate> getIceCandidateSubject() {
        return iceCandidateSubject;
    }

    @NonNull
    public PublishSubject<SessionDescription> getSdpSubject() {
        return sdpSubject;
    }


}
