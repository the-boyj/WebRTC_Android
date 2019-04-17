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

        subscribeCreated();
        subscribeSdp();
        subscribeIceCandidate();
    }

    // Callee의 FCM 수신 이후 시그널링 서버에서 ACK : Caller 커넥션 생성 시점
    private void subscribeCreated() {
/*        addDisposable(
                signalingClient.getCreatedPayloadSubject()
                        .map(CreatedPayload::getCalleeId)
                        .subscribe(this::createPeerConnection)
        );*/
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

        // Offer, Answer를 받은 이후 커넥션으로 연결
        addDisposable(
                signalingClient.getSdpPayloadSubject()
                        .subscribe(sdpPayload -> {
                            // A1 -> A2, A2 -> A3 통화 상황에서 A1 -> A3으로 Answer를 보냈을 때
                            // 새로운 커넥션을 만들고 Offer로 설정한다.
                            if (sdpPayload.getType() == SessionDescription.Type.ANSWER &&
                                    !peerConnectionClient.isConnectedById(sdpPayload.getSender())) {
                                createPeerConnection(sdpPayload.getSender());
                                peerConnectionClient.connectOffer(sdpPayload.getSender());
                            }
                            peerConnectionClient.setRemoteSdp(sdpPayload.getSender(), sdpPayload.getSdp());

                            if (sdpPayload.getType() == SessionDescription.Type.OFFER) {
                                peerConnectionClient.createAnswer(sdpPayload.getSender());
                            }
                        })
        );

    }

    private void subscribeIceCandidate() {
        addDisposable(
                peerConnectionClient.getIceCandidatePayloadSubject()
                        .subscribe(signalingClient::emitIceCandidate)
        );

        // P2P 중 IceCandidate의 교환
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
    public PublishSubject<BoyjMediaStream> getRemoteMediaStream() {
        validateInitRTC();

        return peerConnectionClient.getBoyjMediaStreamSubject();
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
        peerConnectionClient.createOffer(callerId);
    }

    @Override
    public void reject() {
        signalingClient.emitReject();
    }

    @Override
    public void bye() {

    }

    private void createPeerConnection(@NonNull final String targetId) {
        peerConnectionClient.createPeerConnection(targetId);
        peerConnectionClient.addStreamToLocalPeer(targetId, userMediaManager.getLocalMediaStream());
    }

    public void hangUp() {
        signalingClient.emitBye();
        release();
    }

    public void dispose(@NonNull final String targetId) {
        peerConnectionClient.dispose(targetId);
    }

    public void release() {
        userMediaManager.stopCapture();
        compositeDisposable.dispose();
        signalingClient.disconnect();
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
