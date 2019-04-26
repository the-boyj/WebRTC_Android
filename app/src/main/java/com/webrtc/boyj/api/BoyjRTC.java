package com.webrtc.boyj.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.webrtc.boyj.api.peer.PeerCallback;
import com.webrtc.boyj.api.peer.PeerConnectionClient;
import com.webrtc.boyj.api.peer.manager.PeerConnectionFactoryManager;
import com.webrtc.boyj.api.peer.manager.UserMediaManager;
import com.webrtc.boyj.api.signalling.SignalingCallback;
import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.api.signalling.payload.EndOfCallPayload;
import com.webrtc.boyj.api.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.signalling.payload.RejectPayload;
import com.webrtc.boyj.api.signalling.payload.SdpPayload;
import com.webrtc.boyj.data.model.BoyjMediaStream;

import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import io.reactivex.subjects.PublishSubject;

@SuppressWarnings("SpellCheckingInspection")
public class BoyjRTC implements BoyjContract, PeerCallback, SignalingCallback {
    private UserMediaManager userMediaManager;
    private PeerConnectionClient peerConnectionClient;
    private SignalingClient signalingClient;
    @NonNull
    private final PublishSubject<BoyjMediaStream> remoteMediaStream = PublishSubject.create();
    @NonNull
    private final PublishSubject<String> rejectNameSubject = PublishSubject.create();
    @NonNull
    private final PublishSubject<String> endOfCallSubject = PublishSubject.create();

    private static final String ERROR_INITIALIZED = "BoyjRTC is not init. call `initRTC()` before use";

    private boolean isInitialized = false;

    /**
     * PeerConnection 사용을 위한 기본 설정. 호출하지 않으면 RTC 기능 사용시 Runtime Exception 발생
     */
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

    @NonNull
    public MediaStream getLocalMediaStream() {
        validateInitRTC();
        return userMediaManager.getLocalMediaStream();
    }

    @NonNull
    public PublishSubject<BoyjMediaStream> remoteMediaStreamSubject() {
        return remoteMediaStream;
    }

    @NonNull
    public PublishSubject<String> rejectNameSubject() {
        return rejectNameSubject;
    }

    @NonNull
    public PublishSubject<String> endOfCallSubject() {
        return endOfCallSubject;
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

    /**
     * @throws IllegalStateException initRTC()가 호출되지 않은 경우 발생
     */
    private void validateInitRTC() {
        if (!isInitialized) {
            throw new IllegalStateException(ERROR_INITIALIZED);
        }
    }

    @Override
    public void onOfferSdpPayloadFromPeer(@NonNull SdpPayload sdpPayload) {
        signalingClient.emitAccept(sdpPayload);
    }

    @Override
    public void onAnswerSdpPayloadFromPeer(@NonNull SdpPayload sdpPayload) {
        signalingClient.emitAnswer(sdpPayload);
    }

    @Override
    public void onIceCandidatePayloadFromPeer(@NonNull IceCandidatePayload iceCandidatePayload) {
        signalingClient.emitIceCandidate(iceCandidatePayload);
    }

    @Override
    public void onRemoteStreamFromPeer(@NonNull BoyjMediaStream mediaStream) {
        remoteMediaStream.onNext(mediaStream);
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
        rejectNameSubject.onNext(rejectPayload.getSender());
    }

    @Override
    public void onEndOfCallPayloadFromSig(@NonNull EndOfCallPayload endOfCallPayload) {
        peerConnectionClient.dispose(endOfCallPayload.getSender());
        endOfCallSubject.onNext(endOfCallPayload.getSender());
    }
}
