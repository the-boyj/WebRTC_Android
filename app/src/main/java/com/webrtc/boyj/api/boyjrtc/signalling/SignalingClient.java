package com.webrtc.boyj.api.boyjrtc.signalling;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.SignalingCallback;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.DialPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.EndOfCallPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.RejectPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.SdpPayload;
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
        socketIOClient.on(SocketEvent.NOTIFY_REJECT, args -> {
            final RejectPayload payload = JSONUtil.fromJson(args[0], RejectPayload.class);
            callback.onRejectPayloadFromSig(payload);
        });
    }

    private void listenOffer() {
        socketIOClient.on(SocketEvent.RELAY_OFFER, args -> {
            final SdpPayload payload = JSONUtil.fromJson(args[0], SdpPayload.class);
            callback.onOfferSdpPayloadFromSig(payload);
        });
    }

    private void listenAnswer() {
        socketIOClient.on(SocketEvent.RELAY_ANSWER, args -> {
            final SdpPayload payload = JSONUtil.fromJson(args[0], SdpPayload.class);
            callback.onAnswerSdpPayloadFromSig(payload);
        });
    }

    private void listenIceCandidate() {
        socketIOClient.on(SocketEvent.RELAY_ICE_CANDIDATE, args -> {
            final IceCandidatePayload payload = JSONUtil.fromJson(args[0], IceCandidatePayload.class);
            callback.onIceCandidatePayloadFromSig(payload);
        });
    }

    private void listenEndOfCall() {
        socketIOClient.on(SocketEvent.NOTIFY_END_OF_CALL, args -> {
            final EndOfCallPayload payload = JSONUtil.fromJson(args[0], EndOfCallPayload.class);
            callback.onEndOfCallPayloadFromSig(payload);
        });
    }

    public void emitCreateRoom(@NonNull final CreateRoomPayload payload) {
        socketIOClient.emit(SocketEvent.CREATE_ROOM, payload);
    }

    public void emitDial(@NonNull final DialPayload payload) {
        socketIOClient.emit(SocketEvent.DIAL, payload);
    }

    public void emitAwaken(@NonNull final AwakenPayload payload) {
        socketIOClient.emit(SocketEvent.AWAKEN, payload);
    }

    public void emitAccept(@NonNull final SdpPayload payload) {
        socketIOClient.emit(SocketEvent.ACCEPT, payload);
    }

    public void emitAnswer(@NonNull final SdpPayload payload) {
        socketIOClient.emit(SocketEvent.ANSWER, payload);
    }

    public void emitIceCandidate(@NonNull final IceCandidatePayload payload) {
        socketIOClient.emit(SocketEvent.SEND_ICE_CANDIDATE, payload);
    }

    public void emitReject(@NonNull final RejectPayload payload) {
        socketIOClient.emit(SocketEvent.REJECT, payload);
    }

    public void emitEndOfCall() {
        socketIOClient.emit(SocketEvent.END_OF_CALL);
    }

    public void disconnect() {
        socketIOClient.disconnect();
    }
}
