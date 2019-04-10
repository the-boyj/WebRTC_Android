package com.webrtc.boyj.api.peer;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.peer.manager.RtcConfigurationManager;

import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.subjects.PublishSubject;

public class PeerConnectionClient {
    @NonNull
    private final PeerConnection.RTCConfiguration rtcConfiguration;
    @NonNull
    private final MediaConstraints constraints = new MediaConstraints();
    @NonNull
    private final PeerConnectionFactory peerConnectionFactory;
    @NonNull
    private PublishSubject<SessionDescription> sdpSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<IceCandidate> iceCandidateSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<MediaStream> remoteMediaStreamSubject = PublishSubject.create();
    @NonNull
    private Map<String, BoyjPeerConnection> connectionMap = new HashMap<>();

    public PeerConnectionClient(@NonNull final PeerConnectionFactory peerConnectionFactory) {
        this.peerConnectionFactory = peerConnectionFactory;
        this.rtcConfiguration = RtcConfigurationManager.createRtcConfiguration();

        this.constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        this.constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
    }

    public void createPeerConnection(@NonNull final String targetId) {
        final BoyjPeerConnection connection = new BoyjPeerConnection(targetId);
        connection.createPeerConncetion();
        connectionMap.put(targetId, connection);
    }

    public void createOffer(@NonNull final String targetId) {
        Objects.requireNonNull(connectionMap.get(targetId)).createOffer();
    }

    public void createAnswer(@NonNull final String targetId) {
        Objects.requireNonNull(connectionMap.get(targetId)).createAnswer();
    }

    public void setRemoteSdp(@NonNull final String targetId,
                             @NonNull final SessionDescription sdp) {
        Objects.requireNonNull(connectionMap.get(targetId)).setRemoteSdp(sdp);
    }

    public void addIceCandidate(@NonNull final String targetId,
                                @NonNull final IceCandidate iceCandidate) {
        Objects.requireNonNull(connectionMap.get(targetId)).addIceCandidate(iceCandidate);
    }

    public void addStreamToLocalPeer(@NonNull final String targetId,
                                     @NonNull final MediaStream localMediaStream) {
        Objects.requireNonNull(connectionMap.get(targetId)).addStreamToLocalPeer(localMediaStream);
    }

    @NonNull
    public PublishSubject<SessionDescription> getSdpSubject() {
        return sdpSubject;
    }

    @NonNull
    public PublishSubject<IceCandidate> getIceCandidateSubject() {
        return iceCandidateSubject;
    }

    @NonNull
    public PublishSubject<MediaStream> getRemoteMediaStreamSubject() {
        return remoteMediaStreamSubject;
    }

    public void dispose(@NonNull final String targetId) {
        Objects.requireNonNull(connectionMap.get(targetId)).dispose();
    }

    @SuppressWarnings("SpellCheckingInspection")
    private class BoyjPeerConnection {
        @NonNull
        private final String id;
        private PeerConnection connection;

        private BoyjPeerConnection(@NonNull final String id) {
            this.id = id;
        }

        private void createPeerConncetion() {
            connection = peerConnectionFactory.createPeerConnection(
                    rtcConfiguration,
                    new BoyjPeerConnectionObserver(id)
            );
        }

        private void createOffer() {
            connection.createOffer(new BoyjSdpObserver(id), constraints);
        }

        private void createAnswer() {
            connection.createAnswer(new BoyjSdpObserver(id), constraints);
        }

        private void addStreamToLocalPeer(@NonNull final MediaStream localMediaStream) {
            connection.addStream(localMediaStream);
        }

        private void setLocalDescription(@NonNull SessionDescription sdp) {
            connection.setLocalDescription(new CustomSdpObserver(id), sdp);
        }

        private void setRemoteSdp(@NonNull final SessionDescription sdp) {
            connection.setRemoteDescription(new CustomSdpObserver(id), sdp);
        }

        private void addIceCandidate(@NonNull final IceCandidate candidate) {
            connection.addIceCandidate(candidate);
        }

        private void dispose() {
            connection.dispose();
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private class BoyjPeerConnectionObserver extends CustomPeerConnectionObserver {
        @NonNull
        private final String id;

        private BoyjPeerConnectionObserver(@NonNull final String id) {
            this.id = id;
        }

        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            iceCandidateSubject.onNext(iceCandidate);
        }

        @Override
        public void onAddStream(MediaStream mediaStream) {
            remoteMediaStreamSubject.onNext(mediaStream);
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private class BoyjSdpObserver extends CustomSdpObserver {
        @NonNull
        private final String id;

        private BoyjSdpObserver(@NonNull String id) {
            super(id);
            this.id = id;
        }

        @Override
        public void onCreateSuccess(SessionDescription sdp) {
            Objects.requireNonNull(connectionMap.get(id)).setLocalDescription(sdp);
            sdpSubject.onNext(sdp);
        }
    }
}
