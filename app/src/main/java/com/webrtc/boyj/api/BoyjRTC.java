package com.webrtc.boyj.api;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.peer.PeerConnectionClient;
import com.webrtc.boyj.api.peer.manager.UserMediaManager;
import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.api.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.signalling.payload.SdpPayload;
import com.webrtc.boyj.utils.Logger;

import org.webrtc.MediaStream;
import org.webrtc.SessionDescription;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

public class BoyjRTC {
    @NonNull
    private final static SignalingClient signalingClient;
    @NonNull
    private final static PeerConnectionClient peerConnectionClient;
    @NonNull
    private final static UserMediaManager userMediaManager;
    @NonNull
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    static {
        signalingClient = new SignalingClient();
        peerConnectionClient = new PeerConnectionClient();
        userMediaManager = new UserMediaManager();
    }

    public BoyjRTC() {
    }

    public void startCapture() {
        userMediaManager.startCapture();
    }

    public void stopCapture() {
        userMediaManager.stopCapture();
    }

    public MediaStream getUserMedia() {
        return userMediaManager.getUserMedia();
    }

    public PublishSubject<MediaStream> remoteMediaStream() {
        return peerConnectionClient.getRemoteMediaStreamSubject();
    }

    public void readyToCall(@NonNull final boolean isCaller) {
        compositeDisposable.add(signalingClient.getReadySubject().subscribe(() -> {
            initRTC();
            peerConnectionClient.createPeerConnection();
            peerConnectionClient.addStreamToLocalPeer(getUserMedia());
            if (isCaller) {
                peerConnectionClient.createOffer();
            }
        }));
    }

    //앱 유저로 부터 온 이벤트 처리
    public void dial(@NonNull final DialPayload dialPayload) {
        signalingClient.emitDial(dialPayload);
    }


    public void awaken(@NonNull final AwakenPayload payload) {
        signalingClient.emitAwaken(payload);
    }

    @NonNull
    public CompletableSubject knock() {
        return signalingClient.getKnockSubject();
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
    public CompletableSubject bye(){
        return signalingClient.getByeSubject();
    }


    public void release() {
        userMediaManager.stopCapture();
        compositeDisposable.dispose();
        signalingClient.disconnect();
        peerConnectionClient.release();
    }

    private void initRTC() {
        compositeDisposable.addAll(
                peerConnectionClient.getSdpSubject().subscribe(sessionDescription -> {
                    final SdpPayload sdpPayload = new SdpPayload.Builder(sessionDescription).build();
                    signalingClient.emitSdp(sdpPayload);
                }),
                peerConnectionClient.getIceCandidateSubject().subscribe(iceCandidate -> {
                    IceCandidatePayload iceCandidatePayload = new IceCandidatePayload.Builder(iceCandidate).build();
                    signalingClient.emitIceCandidate(iceCandidatePayload);
                }),
                signalingClient.getSdpSubject().subscribe(sdp -> {
                    Logger.d("getSdpSubject()");
                    peerConnectionClient.setRemoteSdp(sdp);
                    if (sdp.type == SessionDescription.Type.OFFER) {
                        peerConnectionClient.createAnswer();
                    }
                }),
                signalingClient.getIceCandidateSubject().subscribe(candidate -> {
                    Logger.d(candidate.toString());
                    peerConnectionClient.addIceCandidate(candidate);
                })
        );
    }
}
