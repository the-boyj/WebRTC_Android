package com.webrtc.boyj.api.boyjrtc.signalling;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.SignalingCallback;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.DialPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.EndOfCallPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.ParticipantsPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.RejectPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.SdpPayload;
import com.webrtc.boyj.utils.JSONUtil;
import com.webrtc.boyj.utils.Logger;

import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.*;

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
        listenParticipants();
        listenOffer();
        listenAnswer();
        listenIceCandidate();
        listenEndOfCall();
    }

    private void listenReject() {
        socketIOClient.on(NOTIFY_REJECT, args -> {
            final RejectPayload payload = JSONUtil.fromJson(args[0], RejectPayload.class);
            Logger.ii(NOTIFY_REJECT.toString(), payload.toString());
            callback.onRejectPayloadFromSig(payload);
        });
    }

    private void listenParticipants() {
        socketIOClient.on(PARTICIPANTS, args -> {
            final ParticipantsPayload payload = JSONUtil.fromJson(args[0], ParticipantsPayload.class);
            Logger.ii(PARTICIPANTS.toString(), payload.toString());
            callback.onParticipantsPayloadFromSig(payload);
        });
    }

    private void listenOffer() {
        socketIOClient.on(RELAY_OFFER, args -> {
            final SdpPayload payload = JSONUtil.fromJson(args[0], SdpPayload.class);
            Logger.ii(RELAY_OFFER.toString(), payload.toString());
            callback.onOfferSdpPayloadFromSig(payload);
        });
    }

    private void listenAnswer() {
        socketIOClient.on(RELAY_ANSWER, args -> {
            final SdpPayload payload = JSONUtil.fromJson(args[0], SdpPayload.class);
            Logger.ii(RELAY_ANSWER.toString(), payload.toString());
            callback.onAnswerSdpPayloadFromSig(payload);
        });
    }

    private void listenIceCandidate() {
        socketIOClient.on(RELAY_ICE_CANDIDATE, args -> {
            final IceCandidatePayload payload = JSONUtil.fromJson(args[0], IceCandidatePayload.class);
            Logger.ii(RELAY_ICE_CANDIDATE.toString(), payload.toString());
            callback.onIceCandidatePayloadFromSig(payload);
        });
    }

    private void listenEndOfCall() {
        socketIOClient.on(NOTIFY_END_OF_CALL, args -> {
            final EndOfCallPayload payload = JSONUtil.fromJson(args[0], EndOfCallPayload.class);
            Logger.ii(NOTIFY_END_OF_CALL.toString(), payload.toString());
            callback.onEndOfCallPayloadFromSig(payload);
        });
    }

    public void emitCreateRoom(@NonNull final CreateRoomPayload payload) {
        socketIOClient.emit(CREATE_ROOM, payload);
    }

    public void emitDial(@NonNull final DialPayload payload) {
        socketIOClient.emit(DIAL, payload);
    }

    public void emitAwaken(@NonNull final AwakenPayload payload) {
        socketIOClient.emit(AWAKEN, payload);
    }

    public void emitAccept() {
        socketIOClient.emit(ACCEPT);
    }

    public void emitOffer(@NonNull final SdpPayload payload) {
        socketIOClient.emit(OFFER, payload);
    }

    public void emitAnswer(@NonNull final SdpPayload payload) {
        socketIOClient.emit(ANSWER, payload);
    }

    public void emitIceCandidate(@NonNull final IceCandidatePayload payload) {
        socketIOClient.emit(SEND_ICE_CANDIDATE, payload);
    }

    public void emitReject(@NonNull final RejectPayload payload) {
        socketIOClient.emit(REJECT, payload);
    }

    public void emitEndOfCall() {
        socketIOClient.emit(END_OF_CALL);
    }

    public void disconnect() {
        socketIOClient.disconnect();
    }
}
