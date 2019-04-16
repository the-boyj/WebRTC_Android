package com.webrtc.boyj.api;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.peer.PeerConnectionClient;
import com.webrtc.boyj.api.peer.manager.PeerConnectionFactoryManager;
import com.webrtc.boyj.api.peer.manager.UserMediaManager;
import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.data.model.BoyjMediaStream;

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
        final PeerConnectionFactory factory =
                PeerConnectionFactoryManager.createPeerConnectionFactory();

        userMediaManager = new UserMediaManager(factory);
        peerConnectionClient = new PeerConnectionClient(factory);

        compositeDisposable.addAll(
                // Callee의 FCM 수신 이후 시그널링 서버에서 ACK : Caller 커넥션 생성 시점
                signalingClient.getCreatedPayloadSubject().subscribe(createdPayload -> {
                    createPeerConnection(createdPayload.getCalleeId());
                }),

                // Offer, Answer를 만들고 난 이후 시그널링 서버로 전송
                peerConnectionClient.getSdpPayloadSubject().subscribe(sdpPayload -> {
                    if (sdpPayload.getType() == SessionDescription.Type.OFFER) {
                        signalingClient.emitAccept(sdpPayload);
                    } else if (sdpPayload.getType() == SessionDescription.Type.ANSWER) {
                        signalingClient.emitAnswer(sdpPayload);
                    }
                }),

                // Offer, Answer를 받은 이후 커넥션으로 연결
                signalingClient.getSdpPayloadSubject().subscribe(sdpPayload -> {
                    // A1 -> A2, A2 -> A3 통화 상황에서 A1 -> A3으로 Answer를 보냈을 때
                    // 새로운 커넥션을 만들고 Offer로 설정한다.
                    if (sdpPayload.getType() == SessionDescription.Type.ANSWER &&
                            !peerConnectionClient.isConnectedById(sdpPayload.getSender())) {
                        peerConnectionClient.createPeerConnection(sdpPayload.getSender());
                        peerConnectionClient.connectOffer(sdpPayload.getSender());
                    }
                    peerConnectionClient.setRemoteSdp(sdpPayload.getSender(), sdpPayload.getSdp());

                    if (sdpPayload.getType() == SessionDescription.Type.OFFER) {
                        peerConnectionClient.createAnswer(sdpPayload.getSender());
                    }
                }),

                peerConnectionClient.getIceCandidatePayloadSubject().subscribe(iceCandidatePayload -> {
                    signalingClient.emitIceCandidate(iceCandidatePayload);
                }),

                // P2P 통신 중 IceCandidate의 교환
                signalingClient.getIceCandidatePayloadSubject().subscribe(iceCandidatePayload -> {
                    peerConnectionClient.addIceCandidate(
                            iceCandidatePayload.getSender(),
                            iceCandidatePayload.getIceCandidate()
                    );
                })
        );
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
    public PublishSubject<BoyjMediaStream> remoteMediaStream() {
        return peerConnectionClient.getBoyjMediaStreamSubject();
    }

    /**
     * 처음으로 통화를 요청할 경우 room을 생성한다.
     *
     * @param payload room, callerId 가 담긴 페이로드
     */
    public void createRoom(@NonNull final CreateRoomPayload payload) {
        signalingClient.emitCreateRoom(payload);
    }

    /**
     * Caller가 상대방에게 통화를 요청한다.
     *
     * @param payload calleeId가 담긴 페이로드
     */
    public void dial(@NonNull final DialPayload payload) {
        signalingClient.emitDial(payload);
    }

    /**
     * Callee가 푸시 알람을 수신 후 서버로 응답한다.
     *
     * @param payload room, calleeId가 담긴 페이로드
     */
    public void awaken(@NonNull final AwakenPayload payload) {
        signalingClient.emitAwaken(payload);
    }

    /**
     * Callee가 전화를 승인한 후 커넥션 생성 및 Accept를 서버로 전송한다.
     *
     * @param callerId 전화가 걸려온 상대 아이디
     */
    public void accept(@NonNull final String callerId) {
        createPeerConnection(callerId);
        peerConnectionClient.createOffer(callerId);
    }

    private void createPeerConnection(@NonNull final String targetId) {
        peerConnectionClient.createPeerConnection(targetId);
        peerConnectionClient.addStreamToLocalPeer(targetId, getUserMedia());
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
