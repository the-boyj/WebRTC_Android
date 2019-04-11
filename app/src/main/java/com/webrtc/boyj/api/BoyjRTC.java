package com.webrtc.boyj.api;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.peer.PeerConnectionClient;
import com.webrtc.boyj.api.peer.manager.PeerConnectionFactoryManager;
import com.webrtc.boyj.api.peer.manager.UserMediaManager;
import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.utils.Logger;

import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

public class BoyjRTC {
    @NonNull
    private SignalingClient signalingClient = new SignalingClient();
    private PeerConnectionClient peerConnectionClient;
    private UserMediaManager userMediaManager;
    @NonNull
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void initRTC() {
        final PeerConnectionFactory factory = PeerConnectionFactoryManager.createPeerConnectionFactory();
        userMediaManager = new UserMediaManager(factory);
        peerConnectionClient = new PeerConnectionClient(factory);

        compositeDisposable.addAll(
                // Todo : SDP, IceCandidate 페이로드 변경 후 타겟 설정
                /*
                peerConnectionClient.getSdpSubject().subscribe(sessionDescription -> {
                    final SdpPayload sdpPayload = new SdpPayload.Builder(sessionDescription).build();
                    signalingClient.emitSdp(sdpPayload);
                }),
                peerConnectionClient.getIceCandidateSubject().subscribe(iceCandidate -> {
                    final IceCandidatePayload iceCandidatePayload = new IceCandidatePayload.Builder(iceCandidate).build();
                    signalingClient.emitIceCandidate(iceCandidatePayload);
                })

                signalingClient.getSdpSubject().subscribe(sdp -> {
                    peerConnectionClient.setRemoteSdp(sdp);
                    if (sdp.type == SessionDescription.Type.OFFER) {
                        peerConnectionClient.createAnswer();
                    }
                }),
                signalingClient.getIceCandidateSubject().subscribe(candidate -> {
                    Logger.d(candidate.toString());
                    peerConnectionClient.addIceCandidate(candidate);
                })
                */
        );
    }

    // Todo : accept이후 offer 연결
    public void createOffer(@NonNull final String targetId) {
        peerConnectionClient.createPeerConnection(targetId);
        peerConnectionClient.addStreamToLocalPeer(targetId, getUserMedia());
        peerConnectionClient.createOffer(targetId);
    }

    // Todo : sdp를 받은 이후 OFFER에게 온 경우면 호출
    private void createAnswer(@NonNull final String targetId,
                              @NonNull final SessionDescription sdp) {
        peerConnectionClient.createPeerConnection(targetId);
        peerConnectionClient.setRemoteSdp(targetId, sdp);
        peerConnectionClient.addStreamToLocalPeer(targetId, getUserMedia());
        peerConnectionClient.createAnswer(targetId);
    }

    public void startCapture() {
        userMediaManager.startCapture();
    }

    public void stopCapture() {
        userMediaManager.stopCapture();
    }

    @NonNull
    public MediaStream getUserMedia() {
        return userMediaManager.getLocalMediaStream();
    }

    @NonNull
    public PublishSubject<MediaStream> remoteMediaStream() {
        return peerConnectionClient.getRemoteMediaStreamSubject();
    }

    /**
     * 처음으로 통화를 요청할 경우 room을 생성한다.
     *
     * @param payload room, callerId 가 담긴 페이로드
     */
    public void createRoom(@NonNull final CreateRoomPayload payload) {
        Logger.i(payload.toString());
        signalingClient.emitCreateRoom(payload);
    }

    /**
     * Caller가 상대방에게 통화를 요청한다.
     *
     * @param payload calleeId가 담긴 페이로드
     */
    public void dial(@NonNull final DialPayload payload) {
        Logger.i(payload.toString());
        signalingClient.emitDial(payload);
    }

    /**
     * Callee가 푸시 알람을 수신 후 서버로 응답한다.
     *
     * @param payload room, calleeId가 담긴 페이로드
     */
    public void awaken(@NonNull final AwakenPayload payload) {
        Logger.i(payload.toString());
        signalingClient.emitAwaken(payload);
    }

    public void accept() {
        signalingClient.emitAccept();
    }

    public void reject() {
        signalingClient.emitReject();
    }

    public void hangUp() {
        signalingClient.emitBye();
        release();
    }

    @NonNull
    public CompletableSubject bye() {
        return signalingClient.getByeSubject();
    }

    public void dispose(@NonNull final String targetId) {
        peerConnectionClient.dispose(targetId);
    }

    public void release() {
        userMediaManager.stopCapture();
        compositeDisposable.dispose();
        signalingClient.disconnect();
    }
}
