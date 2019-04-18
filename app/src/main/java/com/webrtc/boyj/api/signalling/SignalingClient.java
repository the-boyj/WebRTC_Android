package com.webrtc.boyj.api.signalling;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.EndOfCallPayload;
import com.webrtc.boyj.api.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.api.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.signalling.payload.RejectPayload;
import com.webrtc.boyj.api.signalling.payload.SdpPayload;
import com.webrtc.boyj.utils.JSONUtil;
import com.webrtc.boyj.utils.Logger;

import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.subjects.PublishSubject;
import io.socket.emitter.Emitter;

public class SignalingClient {
    private static final String CREATE_ROOM = "CREATE_ROOM";
    private static final String DIAL = "DIAL";
    private static final String ANSWER = "ANSWER";
    private static final String NOTIFY_REJECT = "NOTIFY_REJECT";
    private static final String RELAY_OFFER = "RELAY_OFFER";

    private static final String AWAKEN = "AWAKEN";
    private static final String ACCEPT = "ACCEPT";
    private static final String REJECT = "REJECT";
    private static final String RELAY_ANSWER = "RELAY_ANSWER";

    private static final String SEND_ICE_CANDIDATE = "SEND_ICE_CANDIDATE";
    private static final String RELAY_ICE_CANDIDATE = "RELAY_ICE_CANDIDATE";
    private static final String END_OF_CALL = "END_OF_CALL";
    private static final String NOTIFY_END_OF_CALL = "NOTIFY_END_OF_CALL";
    private static final String PEER_TO_SERVER_ERROR = "PEER_TO_SERVER_ERROR";
    private static final String SERVER_TO_PEER_ERROR = "SERVER_TO_PEER_ERROR";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            CREATE_ROOM, DIAL, ANSWER, NOTIFY_REJECT, RELAY_OFFER, /* Caller */
            AWAKEN, ACCEPT, REJECT, RELAY_ANSWER, /* Callee */
            SEND_ICE_CANDIDATE, RELAY_ICE_CANDIDATE, END_OF_CALL, NOTIFY_END_OF_CALL, /* 공통 */
            PEER_TO_SERVER_ERROR, SERVER_TO_PEER_ERROR /* 에러 */
    })
    @interface Event {
    }

    @NonNull
    private static final SocketIOClient socketIOClient = new SocketIOClient();
    @NonNull
    private PublishSubject<IceCandidatePayload> iceCandidatePayloadSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<SdpPayload> sdpPayloadSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<RejectPayload> rejectPayloadSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<EndOfCallPayload> endOfCallPayloadSubject = PublishSubject.create();

    public SignalingClient() {
        listenReject();
        listenSdp();
        listenIceCandidate();
        listenEndOfCall();
        socketIOClient.connect();
    }

    private void listenReject() {
        socketIOClient.on(NOTIFY_REJECT, args -> {
            final RejectPayload payload =
                    (RejectPayload) JSONUtil.fromJson((JSONObject) args[0], RejectPayload.class);
            rejectPayloadSubject.onNext(payload);
        });
    }

    private void listenSdp() {
        final Emitter.Listener sdpListener = args -> {
            final SdpPayload payload =
                    (SdpPayload) JSONUtil.fromJson((JSONObject) args[0], SdpPayload.class);
            Logger.i(payload.toString());
            sdpPayloadSubject.onNext(payload);
        };
        socketIOClient.on(RELAY_OFFER, sdpListener);
        socketIOClient.on(RELAY_ANSWER, sdpListener);
    }

    private void listenIceCandidate() {
        socketIOClient.on(RELAY_ICE_CANDIDATE, args -> {
            final IceCandidatePayload payload =
                    (IceCandidatePayload) JSONUtil.fromJson((JSONObject) args[0], IceCandidatePayload.class);
            Logger.i(payload.toString());
            iceCandidatePayloadSubject.onNext(payload);
        });
    }

    private void listenEndOfCall() {
        socketIOClient.on(NOTIFY_END_OF_CALL, args -> {
            final EndOfCallPayload payload =
                    (EndOfCallPayload) JSONUtil.fromJson((JSONObject) args[0], EndOfCallPayload.class);
            Logger.i(payload.toString());
            endOfCallPayloadSubject.onNext(payload);
        });
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
        socketIOClient.emit(ACCEPT, JSONUtil.toJSONObject(payload));
    }

    public void emitAnswer(@NonNull final SdpPayload payload) {
        Logger.i(payload.toString());
        socketIOClient.emit(ANSWER, JSONUtil.toJSONObject(payload));
    }

    public void emitIceCandidate(@NonNull final IceCandidatePayload payload) {
        Logger.i(payload.toString());
        socketIOClient.emit(SEND_ICE_CANDIDATE, JSONUtil.toJSONObject(payload));
    }

    public void emitReject(@NonNull final RejectPayload payload) {
        Logger.i(payload.toString());
        socketIOClient.emit(REJECT, payload);
    }

    public void emitEndOfCall() {
        socketIOClient.emit(END_OF_CALL);
    }

    public void disconnect() {
        socketIOClient.disconnect();
    }

    @NonNull
    public PublishSubject<RejectPayload> getRejectPayloadSubject() {
        return rejectPayloadSubject;
    }

    @NonNull
    public PublishSubject<SdpPayload> getSdpPayloadSubject() {
        return sdpPayloadSubject;
    }

    @NonNull
    public PublishSubject<IceCandidatePayload> getIceCandidatePayloadSubject() {
        return iceCandidatePayloadSubject;
    }

    @NonNull
    public PublishSubject<EndOfCallPayload> getEndOfCallPayloadSubject() {
        return endOfCallPayloadSubject;
    }
}
