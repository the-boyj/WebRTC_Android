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
import com.webrtc.boyj.api.boyjrtc.signalling.payload.RejectPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.SdpPayload;

import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

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
        PeerConnectionFactoryManager.initialize(context);
        final PeerConnectionFactory factory =
                PeerConnectionFactoryManager.createPeerConnectionFactory();

        userMediaManager = new UserMediaManager(context, factory);
        peerConnectionClient = new PeerConnectionClient(factory, this);
        signalingClient = new SignalingClient(this);
        isInitialized = true;

        startCapture();
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
    public void accept(@NonNull final String callerId) {
        validateInitRTC();
        createPeerConnection(callerId);
        createOffer(callerId);
    }

    private void createPeerConnection(@NonNull final String targetId) {
        peerConnectionClient.createPeerConnection(targetId);
        peerConnectionClient.addLocalStream(targetId, userMediaManager.getLocalMediaStream());
    }

    private void createOffer(@NonNull final String targetId) {
        peerConnectionClient.createOffer(targetId);
    }

    @Override
    public void reject(@NonNull final RejectPayload payload) {
        signalingClient.emitReject(payload);
        disconnect();
    }

    @Override
    public void endOfCall() {
        signalingClient.emitEndOfCall();
    }

    private void disconnect() {
        signalingClient.disconnect();
    }

    public void release() {
        peerConnectionClient.disposeAll();
        userMediaManager.stopCapture();
        signalingClient.disconnect();
    }

    private void validateInitRTC() {
        if (!isInitialized) {
            throw new IllegalStateException(ERROR_INITIALIZED);
        }
    }

    @Override
    public void onOfferSdpPayloadFromPeer(@NonNull SdpPayload sdpPayload) {
        peerConnectionClient.setLocalDescription(sdpPayload.getReceiver(), sdpPayload.getSdp());
        signalingClient.emitAccept(sdpPayload);
    }

    @Override
    public void onAnswerSdpPayloadFromPeer(@NonNull SdpPayload sdpPayload) {
        peerConnectionClient.setLocalDescription(sdpPayload.getReceiver(), sdpPayload.getSdp());
        signalingClient.emitAnswer(sdpPayload);
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
    public void onOfferSdpPayloadFromSig(@NonNull SdpPayload sdpPayload) {
        createPeerConnection(sdpPayload.getSender());
        createAnswer(sdpPayload.getSender());
        setRemoteSdp(sdpPayload.getSender(), sdpPayload.getSdp());
    }

    private void createAnswer(@NonNull final String targetId) {
        peerConnectionClient.createAnswer(targetId);
    }

    private void setRemoteSdp(@NonNull final String targetId,
                              @NonNull final SessionDescription sdp) {
        peerConnectionClient.setRemoteSdp(targetId, sdp);
    }

    @Override
    public void onCallFinish() {
        publisher.completeEndOfCall();
    }

    @Override
    public void onAnswerSdpPayloadFromSig(@NonNull SdpPayload sdpPayload) {
        if (!peerConnectionClient.isConnectedById(sdpPayload.getSender())) {
            createPeerConnection(sdpPayload.getSender());
            peerConnectionClient.connectOffer(sdpPayload.getSender());
        }
        setRemoteSdp(sdpPayload.getSender(), sdpPayload.getSdp());
    }

    @Override
    public void onIceCandidatePayloadFromSig(@NonNull IceCandidatePayload iceCandidatePayload) {
        peerConnectionClient.addIceCandidate(
                iceCandidatePayload.getSender(),
                iceCandidatePayload.getIceCandidate());
    }

    @Override
    public void onRejectPayloadFromSig(@NonNull RejectPayload rejectPayload) {
        publisher.submitReject(rejectPayload.getSender());
    }

    @Override
    public void onEndOfCallPayloadFromSig(@NonNull EndOfCallPayload endOfCallPayload) {
        publisher.submitLeave(endOfCallPayload.getSender());
        peerConnectionClient.dispose(endOfCallPayload.getSender());
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
