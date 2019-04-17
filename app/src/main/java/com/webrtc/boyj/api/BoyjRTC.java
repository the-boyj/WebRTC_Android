package com.webrtc.boyj.api;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.peer.PeerConnectionClient;
import com.webrtc.boyj.api.peer.manager.PeerConnectionFactoryManager;
import com.webrtc.boyj.api.peer.manager.UserMediaManager;
import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.EndOfCallPayload;
import com.webrtc.boyj.api.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.api.signalling.payload.RejectPayload;
import com.webrtc.boyj.data.model.BoyjMediaStream;

import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

@SuppressWarnings("SpellCheckingInspection")
public class BoyjRTC implements BoyjContract {
    private PeerConnectionClient peerConnectionClient;
    private UserMediaManager userMediaManager;
    private final SignalingClient signalingClient = new SignalingClient();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final String ERROR_INITIALIZED = "BoyjRTC is not init. call `initRTC()` before use";

    /**
     * initRTC()가 호출되면 true로 변경된다.
     */
    private boolean isInitialized = false;

    /**
     * PeerConnection 사용을 위한 기본 설정. 호출하지 않으면 RTC 기능 사용시 Runtime Exception 발생
     */
    public void initRTC() {
        final PeerConnectionFactory factory =
                PeerConnectionFactoryManager.createPeerConnectionFactory();

        userMediaManager = new UserMediaManager(factory);
        peerConnectionClient = new PeerConnectionClient(factory);
        isInitialized = true;

        subscribeSdp();
        subscribeIceCandidate();
        subscribeEndOfCall();
    }

    private void subscribeSdp() {
        // Offer, Answer를 만들고 난 이후 시그널링 서버로 전송
        addDisposable(
                peerConnectionClient.getSdpPayloadSubject()
                        .subscribe(sdpPayload -> {
                            if (sdpPayload.getType() == SessionDescription.Type.OFFER) {
                                signalingClient.emitAccept(sdpPayload);
                            } else if (sdpPayload.getType() == SessionDescription.Type.ANSWER) {
                                signalingClient.emitAnswer(sdpPayload);
                            }
                        })
        );

        /*
         * 참고 : A1 -> A2 || A2 -> A3의 총 3명에 대한 통화 상황을 가정. Answer를 전달받은 상황에서
         * 커넥션이 만들어져있지 않다면 이는 A1 - A3의 추가 연결을 뜻한다. 이런 경우 Peer Client가
         * 가지고 있는 offer sdp를 이용해 새로운 커넥션을 만든다.
         */
        addDisposable(
                signalingClient.getSdpPayloadSubject()
                        .subscribe(sdpPayload -> {
                            if (sdpPayload.getType() == SessionDescription.Type.OFFER) {
                                createPeerConnection(sdpPayload.getSender());
                                createAnswer(sdpPayload.getSender());
                            } else if (sdpPayload.getType() == SessionDescription.Type.ANSWER) {
                                if (!peerConnectionClient.isConnectedById(sdpPayload.getSender())) {
                                    createPeerConnection(sdpPayload.getSender());
                                    peerConnectionClient.connectOffer(sdpPayload.getSender());
                                }
                            }
                            peerConnectionClient.setRemoteSdp(sdpPayload.getSender(), sdpPayload.getSdp());
                        })
        );
    }

    private void subscribeIceCandidate() {
        addDisposable(
                peerConnectionClient.getIceCandidatePayloadSubject()
                        .subscribe(signalingClient::emitIceCandidate)
        );

        addDisposable(
                signalingClient.getIceCandidatePayloadSubject()
                        .subscribe(iceCandidatePayload ->
                                peerConnectionClient.addIceCandidate(
                                        iceCandidatePayload.getSender(),
                                        iceCandidatePayload.getIceCandidate()
                                )
                        )
        );
    }

    private void subscribeEndOfCall() {
        addDisposable(
                signalingClient.getEndOfCallPayloadSubject()
                        .subscribe(endOfCallPayload ->
                                peerConnectionClient.dispose(endOfCallPayload.getSender()))
        );
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
    public PublishSubject<RejectPayload> getRejectSubject() {
        return signalingClient.getRejectPayloadSubject();
    }

    @NonNull
    public MediaStream getLocalStream() {
        validateInitRTC();
        return userMediaManager.getLocalMediaStream();
    }

    @NonNull
    public PublishSubject<BoyjMediaStream> getRemoetStreamSubject() {
        validateInitRTC();
        return peerConnectionClient.getBoyjMediaStreamSubject();
    }

    @NonNull
    public PublishSubject<EndOfCallPayload> getByeSubject() {
        validateInitRTC();
        return signalingClient.getEndOfCallPayloadSubject();
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

    @Override
    public void reject(@NonNull final RejectPayload payload) {
        signalingClient.emitReject(payload);
        disconnect();
    }

    @Override
    public void endOfCall() {
        signalingClient.emitEndOfCall();
    }

    private void createPeerConnection(@NonNull final String targetId) {
        peerConnectionClient.createPeerConnection(targetId);
        peerConnectionClient.addStreamToLocalPeer(targetId, userMediaManager.getLocalMediaStream());
    }

    private void createOffer(@NonNull final String targetId) {
        peerConnectionClient.createOffer(targetId);
    }

    private void createAnswer(@NonNull final String targetId) {
        peerConnectionClient.createAnswer(targetId);
    }

    private void disconnect() {
        signalingClient.disconnect();
    }

    public void release() {
        peerConnectionClient.disposeAll();
        userMediaManager.stopCapture();
        signalingClient.disconnect();
        compositeDisposable.dispose();
    }

    private void addDisposable(@NonNull final Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    /**
     * @throws IllegalStateException initRTC()가 호출되지 않은 경우 발생
     */
    private void validateInitRTC() {
        if (!isInitialized) {
            throw new IllegalStateException(ERROR_INITIALIZED);
        }
    }
}
