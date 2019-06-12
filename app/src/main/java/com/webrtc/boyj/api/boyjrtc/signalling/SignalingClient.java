package com.webrtc.boyj.api.boyjrtc.signalling;

import androidx.annotation.NonNull;

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

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.ACCEPT;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.ANSWER;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.AWAKEN;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.CONNECTION_ACK;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.CREATE_ROOM;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.DIAL;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.END_OF_CALL;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.NOTIFY_END_OF_CALL;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.NOTIFY_REJECT;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.OFFER;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.PARTICIPANTS;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.REJECT;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.RELAY_ANSWER;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.RELAY_ICE_CANDIDATE;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.RELAY_OFFER;
import static com.webrtc.boyj.api.boyjrtc.signalling.SocketEvent.SEND_ICE_CANDIDATE;

public class SignalingClient {
    @NonNull
    private static final SocketIO socketIO = SocketIO.create();

    @NonNull
    private PublishSubject<String> connectionAck = PublishSubject.create();

    @NonNull
    private final PublishSubject<RejectPayload> rejectSubject = PublishSubject.create();
    @NonNull
    private final PublishSubject<ParticipantsPayload> participantsSubject = PublishSubject.create();
    @NonNull
    private final PublishSubject<SdpPayload> offerSubject = PublishSubject.create();
    @NonNull
    private final PublishSubject<SdpPayload> answerSubject = PublishSubject.create();
    @NonNull
    private final PublishSubject<IceCandidatePayload> iceCandidateSubject = PublishSubject.create();
    @NonNull
    private final PublishSubject<EndOfCallPayload> endOfCallSubject = PublishSubject.create();

    public SignalingClient() {
        socketIO.connect();
    }

    public void listenSocket() {
        listenConnectionAck();
        listenReject();
        listenParticipants();
        listenOffer();
        listenAnswer();
        listenIceCandidate();
        listenEndOfCall();
    }

    public Observable<String> connectionAck() {
        return connectionAck;
    }

    private void listenConnectionAck() {
        socketIO.on(CONNECTION_ACK, args -> {
            connectionAck.onNext("connect");
        });
    }

    private void listenReject() {
        socketIO.on(NOTIFY_REJECT, args -> {
            final RejectPayload payload = JSONUtil.fromJson(args[0], RejectPayload.class);
            Logger.BOYJ(NOTIFY_REJECT.toString());
            Logger.ii(NOTIFY_REJECT.toString(), payload.toString());
            rejectSubject.onNext(payload);
        });
    }

    @NonNull
    public Observable<RejectPayload> reject() {
        return rejectSubject.hide();
    }

    private void listenParticipants() {
        socketIO.on(PARTICIPANTS, args -> {
            final ParticipantsPayload payload = JSONUtil.fromJson(args[0], ParticipantsPayload.class);
            Logger.BOYJ(PARTICIPANTS.toString());
            Logger.ii(PARTICIPANTS.toString(), payload.toString());
            participantsSubject.onNext(payload);
        });
    }

    @NonNull
    public Observable<ParticipantsPayload> participants() {
        return participantsSubject.hide();
    }

    private void listenOffer() {
        socketIO.on(RELAY_OFFER, args -> {
            final SdpPayload payload = JSONUtil.fromJson(args[0], SdpPayload.class);
            Logger.BOYJ(RELAY_OFFER.toString());
            Logger.ii(RELAY_OFFER.toString(), payload.toString());
            offerSubject.onNext(payload);
        });
    }

    @NonNull
    public Observable<SdpPayload> offer() {
        return offerSubject.hide();
    }

    private void listenAnswer() {
        socketIO.on(RELAY_ANSWER, args -> {
            final SdpPayload payload = JSONUtil.fromJson(args[0], SdpPayload.class);
            Logger.BOYJ(RELAY_ANSWER.toString());
            Logger.ii(RELAY_ANSWER.toString(), payload.toString());
            answerSubject.onNext(payload);
        });
    }

    @NonNull
    public Observable<SdpPayload> answer() {
        return answerSubject.hide();
    }

    private void listenIceCandidate() {
        socketIO.on(RELAY_ICE_CANDIDATE, args -> {
            final IceCandidatePayload payload = JSONUtil.fromJson(args[0], IceCandidatePayload.class);
            Logger.BOYJ(RELAY_ICE_CANDIDATE.toString());
            Logger.ii(RELAY_ICE_CANDIDATE.toString(), payload.toString());
            iceCandidateSubject.onNext(payload);
        });
    }

    @NonNull
    public Observable<IceCandidatePayload> iceCandidate() {
        return iceCandidateSubject.hide();
    }

    private void listenEndOfCall() {
        socketIO.on(NOTIFY_END_OF_CALL, args -> {
            final EndOfCallPayload payload = JSONUtil.fromJson(args[0], EndOfCallPayload.class);
            Logger.BOYJ(NOTIFY_END_OF_CALL.toString());
            Logger.ii(NOTIFY_END_OF_CALL.toString(), payload.toString());
            endOfCallSubject.onNext(payload);
        });
    }

    @NonNull
    public Observable<EndOfCallPayload> endOfCall() {
        return endOfCallSubject.hide();
    }

    public void emitCreateRoom(@NonNull final CreateRoomPayload payload) {
        Logger.ii(CREATE_ROOM.toString(), payload.toString());
        socketIO.emit(CREATE_ROOM, payload);
    }

    public void emitDial(@NonNull final DialPayload payload) {
        Logger.ii(DIAL.toString(), payload.toString());
        socketIO.emit(DIAL, payload);
    }

    public void emitAwaken(@NonNull final AwakenPayload payload) {
        Logger.ii(AWAKEN.toString(), payload.toString());
        socketIO.emit(AWAKEN, payload);
    }

    public void emitAccept() {
        Logger.i(ACCEPT.toString());
        socketIO.emit(ACCEPT);
    }

    public void emitOffer(@NonNull final SdpPayload payload) {
        Logger.ii(OFFER.toString(), payload.toString());
        socketIO.emit(OFFER, payload);
    }

    public void emitAnswer(@NonNull final SdpPayload payload) {
        Logger.ii(ANSWER.toString(), payload.toString());
        socketIO.emit(ANSWER, payload);
    }

    public void emitIceCandidate(@NonNull final IceCandidatePayload payload) {
        Logger.ii(SEND_ICE_CANDIDATE.toString(), payload.toString());
        socketIO.emit(SEND_ICE_CANDIDATE, payload);
    }

    public void emitReject(@NonNull final RejectPayload payload) {
        Logger.ii(REJECT.toString(), payload.toString());
        socketIO.emit(REJECT, payload);
    }

    public void emitEndOfCall() {
        Logger.i(END_OF_CALL.toString());
        socketIO.emit(END_OF_CALL);
    }

    public void disconnect() {
        socketIO.disconnect();
    }


}
