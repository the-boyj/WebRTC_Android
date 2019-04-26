package com.webrtc.boyj.api.signalling;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.api.signalling.payload.EndOfCallPayload;
import com.webrtc.boyj.api.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.signalling.payload.RejectPayload;
import com.webrtc.boyj.api.signalling.payload.SdpPayload;
import com.webrtc.boyj.utils.JSONUtil;

public class SignalingClient {
    @NonNull
    private static SocketIOClient socketIOClient = new SocketIOClient();
    @NonNull
    private final SignalingCallback callback;

    public SignalingClient(@NonNull SignalingCallback callback) {
        this.callback = callback;
        listenSocket();
        socketIOClient.connect();
    }

    private void listenSocket() {
        listenReject();
        listenOffer();
        listenAnswer();
        listenIceCandidate();
        listenEndOfCall();
    }

    private void listenReject() {
        socketIOClient.on(SignalingEvent.NOTIFY_REJECT, args -> {
            final RejectPayload payload = JSONUtil.fromJson(args[0], RejectPayload.class);
            callback.onRejectPayloadFromSig(payload);
        });
    }

    private void listenOffer() {
        socketIOClient.on(SignalingEvent.RELAY_OFFER, args -> {
            final SdpPayload payload = JSONUtil.fromJson(args[0], SdpPayload.class);
            callback.onOfferSdpPayloadFromSig(payload);
        });
    }

    private void listenAnswer() {
        socketIOClient.on(SignalingEvent.RELAY_ANSWER, args -> {
            final SdpPayload payload = JSONUtil.fromJson(args[0], SdpPayload.class);
            callback.onAnswerSdpPayloadFromSig(payload);
        });
    }

    private void listenIceCandidate() {
        socketIOClient.on(SignalingEvent.RELAY_ICE_CANDIDATE, args -> {
            final IceCandidatePayload payload = JSONUtil.fromJson(args[0], IceCandidatePayload.class);
            callback.onIceCandidatePayloadFromSig(payload);
        });
    }

    private void listenEndOfCall() {
        socketIOClient.on(SignalingEvent.NOTIFY_END_OF_CALL, args -> {
            final EndOfCallPayload payload = JSONUtil.fromJson(args[0], EndOfCallPayload.class);
            callback.onEndOfCallPayloadFromSig(payload);
        });
    }

    public void emitCreateRoom(@NonNull final CreateRoomPayload payload) {
        socketIOClient.emit(SignalingEvent.CREATE_ROOM, payload);
    }

    public void emitDial(@NonNull final DialPayload payload) {
        socketIOClient.emit(SignalingEvent.DIAL, payload);
    }

    public void emitAwaken(@NonNull final AwakenPayload payload) {
        socketIOClient.emit(SignalingEvent.AWAKEN, payload);
    }

    public void emitAccept(@NonNull final SdpPayload payload) {
        socketIOClient.emit(SignalingEvent.ACCEPT, payload);
    }

    public void emitAnswer(@NonNull final SdpPayload payload) {
        socketIOClient.emit(SignalingEvent.ANSWER, payload);
    }

    public void emitIceCandidate(@NonNull final IceCandidatePayload payload) {
        socketIOClient.emit(SignalingEvent.SEND_ICE_CANDIDATE, payload);
    }

    public void emitReject(@NonNull final RejectPayload payload) {
        socketIOClient.emit(SignalingEvent.REJECT, payload);
    }

    public void emitEndOfCall() {
        socketIOClient.emit(SignalingEvent.END_OF_CALL);
    }

    public void disconnect() {
        socketIOClient.disconnect();
    }
}
