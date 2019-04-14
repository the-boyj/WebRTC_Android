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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

public class SignalingClient {
    private static final String CREATE_ROOM = "createRoom";
    private static final String DIAL = "dial";
    private static final String CREATED = "created";
    private static final String ANSWER = "answer";

    private static final String AWAKEN = "awaken";
    private static final String ACCEPT = "accept";

    private static final String RECEIVE_SDP = "rsdp";

    // Todo : 모든 이벤트 추가 후 SocketIOClient의 emit 메소드에 어노테이션 추가
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            CREATE_ROOM, DIAL, CREATED, ANSWER, /* Caller */
            AWAKEN, ACCEPT, /* Callee */
            RECEIVE_SDP /* 공통 */
    })
    private @interface Event {
    }

    @NonNull
    private static final SocketIOClient socketIOClient = new SocketIOClient();
    @NonNull
    private PublishSubject<CreatedPayload> createdPayloadSubject = PublishSubject.create();
    @NonNull
    private CompletableSubject byeSubject = CompletableSubject.create();
    @NonNull
    private PublishSubject<IceCandidate> iceCandidateSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<SdpPayload> sdpPayloadSubject = PublishSubject.create();

    public SignalingClient() {
        socketIOClient.on(CREATED, args -> {
            final CreatedPayload payload =
                    (CreatedPayload) JSONUtil.fromJson((JSONObject) args[0], CreatedPayload.class);
            Logger.i(payload.toString());
            createdPayloadSubject.onNext(payload);
        });
        socketIOClient.on(RECEIVE_SDP, args -> {
            final SdpPayload payload =
                    (SdpPayload) JSONUtil.fromJson((JSONObject) args[0], SdpPayload.class);
            Logger.i(payload.toString());
            sdpPayloadSubject.onNext(payload);
        });
        socketIOClient.on(SignalingEventString.EVENT_RECEIVE_ICE, args -> {
            final IceCandidatePayload payload = IceCandidatePayload.fromJsonObject((JSONObject) args[0]);
            iceCandidateSubject.onNext(payload.getIceCandidate());
        });
        socketIOClient.on(SignalingEventString.EVENT_BYE, args -> byeSubject.onComplete());

        socketIOClient.connect();
    }

    public void emitCreateRoom(@NonNull final CreateRoomPayload payload) {
        Logger.i(payload.toString());
        socketIOClient.emit(CREATE_ROOM, JSONUtil.toJSONObject(payload));
    }

    public void emitDial(@NonNull final DialPayload payload) {
        Logger.i(payload.toString());
        socketIOClient.emit(DIAL, JSONUtil.toJSONObject(payload));
    }

    public void emitAwaken(@NonNull final AwakenPayload payload) {
        Logger.i(payload.toString());
        socketIOClient.emit(AWAKEN, JSONUtil.toJSONObject(payload));
    }

    public void emitAccept(@NonNull final SdpPayload payload) {
        Logger.i(payload.toString());
        socketIOClient.emit(ACCEPT, payload);
    }

    public void emitAnswer(@NonNull final SdpPayload payload) {
        Logger.i(payload.toString());
        socketIOClient.emit(ANSWER, payload);
    }

    public void emitReject() {
        socketIOClient.emit(SignalingEventString.EVENT_REJECT);
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
    public PublishSubject<CreatedPayload> getCreatedPayloadSubject() {
        return createdPayloadSubject;
    }

    @NonNull
    public PublishSubject<SdpPayload> getSdpPayloadSubject() {
        return sdpPayloadSubject;
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
