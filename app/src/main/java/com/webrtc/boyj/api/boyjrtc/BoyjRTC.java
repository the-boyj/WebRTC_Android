package com.webrtc.boyj.api.boyjrtc;

import androidx.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.peer.PeerConnectionClient;
import com.webrtc.boyj.api.boyjrtc.peer.manager.PeerConnectionFactoryManager;
import com.webrtc.boyj.api.boyjrtc.peer.manager.UserMediaManager;
import com.webrtc.boyj.api.boyjrtc.signalling.SignalingClient;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.DialPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.EndOfCallPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.Participant;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.RejectPayload;

import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.CompletableSubject;

public class BoyjRTC implements BoyjContract {
    private static final String ERROR_INITIALIZED = "BoyjRTC is not init. call `initRTC()` before use";


    @NonNull
    private static final PeerConnectionFactory factory;


    private UserMediaManager userMediaManager;
    private PeerConnectionClient peerClient;
    private SignalingClient sigClient;

    @NonNull
    private CompositeDisposable disposables = new CompositeDisposable();

    @NonNull
    private final CompletableSubject onCallFinishSubject = CompletableSubject.create();

    private boolean isInitialized = false;

    static {
        PeerConnectionFactoryManager.initialize();
        factory = PeerConnectionFactoryManager.createPeerConnectionFactory();

    }

    public BoyjRTC() {
        sigClient = new SignalingClient();
    }

    public void initRTC() {
        if (!isInitialized) {
            isInitialized = true;
            userMediaManager = new UserMediaManager(factory);
            peerClient = PeerConnectionClient.of(factory);
            sigClient.listenSocket();
            subscribeSignaling();
            subscribePeer();
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

    private void subscribeSignaling() {
        subscribeParticipantsFromSig();
        subscribeOfferFromSig();
        subscribeAnswerFromSig();
        subscribeIceCandidateFromSig();
        subscribeEndOfCallFromSig();
        subscribeRejectFromSig();
    }

    /**
     * ON EVENT : PARTICIPANTS
     * Offer 생성 순서 확인
     * 1. createPeerConnection
     * 2. addLocalStream
     * 3. createOffer
     */
    private void subscribeParticipantsFromSig() {
        disposables.add(sigClient.participants()
                .subscribe(payload -> {
                            final List<Participant> participants = payload.getParticipants();
                            for (Participant participant : participants) {
                                peerClient.createPeerConnection(participant.getUserId());
                                peerClient.addLocalStream(participant.getUserId(), localStream());
                                peerClient.createOffer(participant.getUserId());
                            }
                        },
                        Throwable::printStackTrace));
    }

    /**
     * ON EVENT : RELAY_OFFER
     * Answer 생성 순서 확인
     * 1. createPeerConnection
     * 2. setLocalStream
     * 3. setRemoteSessionDescription
     * 4. createAnswer
     */
    private void subscribeOfferFromSig() {
        disposables.add(sigClient.offer()
                .subscribe(payload -> {
                    peerClient.createPeerConnection(payload.getSender());
                    peerClient.addLocalStream(payload.getSender(), localStream());
                    peerClient.setRemoteSdp(payload.getSender(), payload.getSdp());
                    peerClient.createAnswer(payload.getSender());
                }, Throwable::printStackTrace));
    }

    /**
     * ON EVENT : RELAY_ANSWER
     * Answer 를 받으면 아직 추가하지 않은 remoteSessionDescription 추가
     */
    private void subscribeAnswerFromSig() {
        disposables.add(sigClient.answer()
                .subscribe(payload ->
                                peerClient.setRemoteSdp(payload.getSender(), payload.getSdp()),
                        Throwable::printStackTrace));
    }

    /**
     * ON EVENT : RELAY_ICE_CANDIDATE
     */
    private void subscribeIceCandidateFromSig() {
        disposables.add(sigClient.iceCandidate()
                .subscribe(payload -> peerClient.addIceCandidate(
                        payload.getSender(),
                        payload.getIceCandidate()),
                        Throwable::printStackTrace));
    }

    /**
     * ON EVENT : NOTIFY_END_OF_CALL
     */
    private void subscribeEndOfCallFromSig() {
        disposables.add(sigClient.endOfCall()
                .subscribe(
                        endOfCallPayload -> {
                            peerClient.dispose(endOfCallPayload.getSender());
                            if (peerClient.getConnectionCount() == 0) {
                                callFinish();
                            }
                        }
                ));

    }

    private void subscribeRejectFromSig() {
        disposables.add(sigClient.reject()
                .subscribe(rejectPayload -> {
                    if (peerClient.getConnectionCount() == 0) {
                        onCallFinishSubject.onComplete();
                        callFinish();
                    }
                }));
    }

    private void subscribePeer() {
        subscribeOfferSdpFromPeer();
        subscribeAnswerSdpFromPeer();
        subscribeIceCandidateFromPeer();
    }

    /**
     * Callback in Offer session description observer
     * EMIT EVENT : OFFER
     */
    private void subscribeOfferSdpFromPeer() {
        disposables.add(peerClient.offer()
                .subscribe(payload -> {
                    peerClient.setLocalSdp(payload.getReceiver(), payload.getSdp());
                    sigClient.emitOffer(payload);
                }, Throwable::printStackTrace));
    }

    /**
     * Callback in Answer session description observer
     * EMIT EVENT : ANSWER
     */
    private void subscribeAnswerSdpFromPeer() {
        disposables.add(peerClient.answer()
                .subscribe(payload -> {
                    peerClient.setLocalSdp(payload.getReceiver(), payload.getSdp());
                    sigClient.emitAnswer(payload);
                }, Throwable::printStackTrace));
    }

    /**
     * Callback in peerconnection observer
     * EMIT EVENT : SEND_ICE_CANDIDATE
     */
    private void subscribeIceCandidateFromPeer() {
        disposables.add(peerClient.iceCandidate()
                .subscribe(payload -> sigClient.emitIceCandidate(payload), Throwable::printStackTrace));
    }


    /**
     * EMIT EVENT : CREATE_ROOM
     */
    @Override
    public void createRoom(@NonNull final CreateRoomPayload payload) {
        sigClient.emitCreateRoom(payload);
    }

    /**
     * EMIT EVENT : DIAL
     */
    @Override
    public void dial(@NonNull final DialPayload payload) {
        sigClient.emitDial(payload);
    }

    /**
     * EMIT_EVENT : AWAKEN
     */
    @Override
    public void awaken(@NonNull final AwakenPayload payload) {
        sigClient.emitAwaken(payload);
    }

    /**
     * EMIT_EVENT : ACCEPT
     */
    @Override
    public void accept() {
        validateInitRTC();
        sigClient.emitAccept();
    }

    /**
     * EMIT EVENT : REJECT
     */
    @Override
    public void reject(@NonNull final RejectPayload payload) {
        sigClient.emitReject(payload);
        disconnect();

    }

    private void disconnect() {
        sigClient.disconnect();
    }

    /**
     * EMIt_EVENT : END_OF_CALL
     */
    @Override
    public void endOfCall() {
        sigClient.emitEndOfCall();
        callFinish();
    }

    private void callFinish() {
        onCallFinishSubject.onComplete();
        release();
    }

    private void release() {
        disposables.dispose();
        peerClient.disposeAll();
        stopCapture();
        disconnect();
    }

    private void validateInitRTC() {
        if (!isInitialized) {
            throw new IllegalStateException(ERROR_INITIALIZED);
        }
    }

    /**
     * ON EVENT : NOTIFY_REJECT
     */
    @NonNull
    public Observable<String> onRejected() {
        return sigClient.reject()
                .map(RejectPayload::getSender);
    }

    /**
     * ON EVENT : NOTIFY_END_OF_CALL
     */
    @NonNull
    public Observable<String> onLeaved() {
        validateInitRTC();
        return sigClient.endOfCall()
                .map(EndOfCallPayload::getSender);
    }

    @NonNull
    public MediaStream localStream() {
        return userMediaManager.mediaStream();
    }

    /**
     * Stream that the other's remote stream
     */
    @NonNull
    public Observable<BoyjMediaStream> remoteStream() {
        return peerClient.remoteMediaStream();
    }

    /**
     * Callback in all stream disposed
     */
    @NonNull
    public Completable onCallFinish() {
        return this.onCallFinishSubject.hide();

    }
}
