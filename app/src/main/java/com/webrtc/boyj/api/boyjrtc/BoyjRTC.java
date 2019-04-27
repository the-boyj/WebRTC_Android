package com.webrtc.boyj.api.boyjrtc;

import android.content.Context;
import android.support.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.peer.PeerConnectionClient;
import com.webrtc.boyj.api.boyjrtc.peer.manager.PeerConnectionFactoryManager;
import com.webrtc.boyj.api.boyjrtc.peer.manager.UserMediaManager;
import com.webrtc.boyj.api.boyjrtc.signalling.SignalingClient;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.DialPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.EndOfCallPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.ParticipantsPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.RejectPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.SdpPayload;

import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class BoyjRTC implements BoyjContract, PeerCallback, SignalingCallback {
    private static final String ERROR_INITIALIZED = "BoyjRTC is not init. call `initRTC()` before use";

    private UserMediaManager userMediaManager;
    private PeerConnectionClient peerConnectionClient;
    private SignalingClient signalingClient;
    @NonNull
    private final BoyjPublisher publisher = new BoyjPublisher();

    private boolean isInitialized = false;

    public void initRTC(@NonNull final Context context) {
        if (!isInitialized) {
            PeerConnectionFactoryManager.initialize(context);
            final PeerConnectionFactory factory =
                    PeerConnectionFactoryManager.createPeerConnectionFactory();

            userMediaManager = new UserMediaManager(context, factory);
            peerConnectionClient = new PeerConnectionClient(factory, this);
            signalingClient = new SignalingClient(this);
            isInitialized = true;

            startCapture();
        }
    }

    public void startCapture() {
        validateInitRTC();
        userMediaManager.startCapture();
    }

    public void stopCapture() {
        validateInitRTC();
        userMediaManager.stopCapture();
    }

    @Override
    public void createRoom(@NonNull final CreateRoomPayload payload) {
        signalingClient.emitCreateRoom(payload);
    }

    @Override
    public void dial(@NonNull final DialPayload payload) {
        signalingClient.emitDial(payload);
    }

    @Override
    public void awaken(@NonNull final AwakenPayload payload) {
        signalingClient.emitAwaken(payload);
    }

    @Override
    public void accept() {
        validateInitRTC();
        signalingClient.emitAccept();
    }

    @Override
    public void reject(@NonNull final RejectPayload payload) {
        signalingClient.emitReject(payload);
        disconnect();
    }

    private void disconnect() {
        signalingClient.disconnect();
    }

    @Override
    public void endOfCall() {
        signalingClient.emitEndOfCall();
        release();
    }

    private void release() {
        peerConnectionClient.disposeAll();
        stopCapture();
        disconnect();
    }

    private void validateInitRTC() {
        if (!isInitialized) {
            throw new IllegalStateException(ERROR_INITIALIZED);
        }
    }

    @Override
    public void onOfferSdpPayloadFromPeer(@NonNull SdpPayload sdpPayload) {
        setLocalDescription(sdpPayload);
        signalingClient.emitOffer(sdpPayload);
    }

    @Override
    public void onAnswerSdpPayloadFromPeer(@NonNull SdpPayload sdpPayload) {
        setLocalDescription(sdpPayload);
        signalingClient.emitAnswer(sdpPayload);
    }

    private void setLocalDescription(@NonNull final SdpPayload sdpPayload) {
        peerConnectionClient.setLocalSdp(sdpPayload.getReceiver(), sdpPayload.getSdp());
    }

    @Override
    public void onIceCandidatePayloadFromPeer(@NonNull IceCandidatePayload iceCandidatePayload) {
        signalingClient.emitIceCandidate(iceCandidatePayload);
    }

    @Override
    public void onRemoteStreamFromPeer(@NonNull BoyjMediaStream mediaStream) {
        publisher.completeCall();
        publisher.submitRemoteStream(mediaStream);
    }

    @Override
    public void onCallFinish() {
        publisher.completeEndOfCall();
        release();
    }

    @Override
    public void onParticipantsPayloadFromSig(@NonNull ParticipantsPayload payload) {
        peerConnectionClient.createOffers(payload.getParticipants(), localStream());
    }

    @Override
    public void onOfferSdpPayloadFromSig(@NonNull SdpPayload payload) {
        peerConnectionClient.createPeerConnection(payload.getSender());
        peerConnectionClient.setRemoteSdp(payload.getSender(), payload.getSdp());
        peerConnectionClient.createAnswer(payload.getSender());
    }

    @Override
    public void onAnswerSdpPayloadFromSig(@NonNull SdpPayload payload) {
        peerConnectionClient.setRemoteSdp(payload.getSender(), payload.getSdp());
    }

    @Override
    public void onIceCandidatePayloadFromSig(@NonNull IceCandidatePayload payload) {
        peerConnectionClient.addIceCandidate(
                payload.getSender(),
                payload.getIceCandidate());
    }

    @Override
    public void onRejectPayloadFromSig(@NonNull RejectPayload payload) {
        publisher.submitReject(payload.getSender());
    }

    @Override
    public void onEndOfCallPayloadFromSig(@NonNull EndOfCallPayload payload) {
        publisher.submitLeave(payload.getSender());
        peerConnectionClient.dispose(payload.getSender());
    }

    @NonNull
    public Observable<String> onRejected() {
        return publisher.getRejectSubject().hide();
    }

    @NonNull
    public Completable onCalled() {
        validateInitRTC();
        return publisher.getCallSubject().hide();
    }

    @NonNull
    public Observable<String> onLeaved() {
        validateInitRTC();
        return publisher.getLeaveSubject().hide();
    }

    @NonNull
    public Completable onEndOfCall() {
        validateInitRTC();
        return publisher.getEndOfCallSubject().hide();
    }

    @NonNull
    public MediaStream localStream() {
        validateInitRTC();
        return userMediaManager.getLocalMediaStream();
    }

    @NonNull
    public Observable<List<BoyjMediaStream>> remoteStreams() {
        validateInitRTC();
        return publisher.getRemoteStreamSubject().hide();
    }
}
