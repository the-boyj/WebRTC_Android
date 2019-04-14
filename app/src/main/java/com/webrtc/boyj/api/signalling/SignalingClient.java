package com.webrtc.boyj.api.signalling;


import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.signalling.payload.CreatedPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.api.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.signalling.payload.SdpPayload;
import com.webrtc.boyj.utils.JSONUtil;
import com.webrtc.boyj.utils.Logger;

import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

public class SignalingClient {
    private static final String CREATE_ROOM = "createRoom";
    private static final String DIAL = "dial";
    private static final String AWAKEN = "awaken";
    private static final String CREATED = "created";

    // Todo : 모든 이벤트 추가 후 SocketIOClient의 emit 메소드에 어노테이션 추가
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({CREATE_ROOM, DIAL, AWAKEN, CREATED})
    private @interface Event {
    }

    @NonNull
    private static final SocketIOClient socketIOClient = new SocketIOClient();
    @NonNull
    private PublishSubject<String> createdSubject = PublishSubject.create();
    @NonNull
    private CompletableSubject byeSubject = CompletableSubject.create();
    @NonNull
    private PublishSubject<IceCandidate> iceCandidateSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<SessionDescription> sdpSubject = PublishSubject.create();

    public SignalingClient() {
        socketIOClient.on(CREATED, args -> {
            final CreatedPayload payload =
                    (CreatedPayload) JSONUtil.fromJson((JSONObject) args[0], CreatedPayload.class);
            Logger.i(payload.toString());
            createdSubject.onNext(payload.getCalleeId());
        });
        socketIOClient.on(SignalingEventString.EVENT_RECEIVE_SDP, args -> {
            final SdpPayload payload = SdpPayload.fromJsonObject((JSONObject) args[0]);
            sdpSubject.onNext(payload.getSdp());
        });
        socketIOClient.on(SignalingEventString.EVENT_RECEIVE_ICE, args -> {
            final IceCandidatePayload payload = IceCandidatePayload.fromJsonObject((JSONObject) args[0]);
            iceCandidateSubject.onNext(payload.getIceCandidate());
        });
        socketIOClient.on(SignalingEventString.EVENT_BYE, args -> byeSubject.onComplete());

        socketIOClient.connect();
    }

    public void emitCreateRoom(@NonNull final CreateRoomPayload payload) {
        socketIOClient.emit(CREATE_ROOM, JSONUtil.toJSONObject(payload));
    }

    public void emitDial(@NonNull final DialPayload payload) {
        socketIOClient.emit(DIAL, JSONUtil.toJSONObject(payload));
    }

    public void emitAwaken(@NonNull final AwakenPayload payload) {
        socketIOClient.emit(AWAKEN, JSONUtil.toJSONObject(payload));
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
    public PublishSubject<String> getCreatedSubject() {
        return createdSubject;
    }

    @NonNull
    public PublishSubject<SessionDescription> getSdpSubject() {
        return sdpSubject;
    }

    @NonNull
    public PublishSubject<IceCandidate> getIceCandidateSubject() {
        return iceCandidateSubject;
    }

    @NonNull
    public CompletableSubject getByeSubject() {
        return byeSubject;
    }
}
