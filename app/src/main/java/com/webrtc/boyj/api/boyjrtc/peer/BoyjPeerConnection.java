package com.webrtc.boyj.api.boyjrtc.peer;

import androidx.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.BoyjMediaStream;
import com.webrtc.boyj.api.boyjrtc.peer.manager.RtcConfigurationManager;
import com.webrtc.boyj.api.boyjrtc.peer.observer.BoyjDefaultSdpObserver;
import com.webrtc.boyj.api.boyjrtc.peer.observer.BoyjPeerConnectionObserver;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.SdpPayload;

import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

class BoyjPeerConnection {
    @NonNull
    private final MediaConstraints constraints = new MediaConstraints();
    @NonNull
    private final Map<String, PeerConnection> connections = new ConcurrentHashMap<>();
    @NonNull
    private final PublishSubject<SdpPayload> offerSdpSubject = PublishSubject.create();
    @NonNull
    private final PublishSubject<SdpPayload> answerSubject = PublishSubject.create();
    @NonNull
    private final PublishSubject<IceCandidatePayload> iceCandidateSubject = PublishSubject.create();
    @NonNull
    private final PublishSubject<BoyjMediaStream> remoteMediaStreamSubject = PublishSubject.create();

    BoyjPeerConnection() {
        this.constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        this.constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
    }

    void createPeerConnection(@NonNull final String id,
                              @NonNull final PeerConnectionFactory factory) {
        final PeerConnection connection = factory.createPeerConnection(
                RtcConfigurationManager.createRtcConfiguration(),
                new BoyjPeerObserver(id));

        if (connection == null) {
            throw new IllegalStateException("PeerConnection is not created");
        }
        connections.put(id, connection);
    }

    void createOffer(@NonNull final String id) {
        getConnectionById(id).createOffer(new BoyjSdpObserver(id), constraints);
    }

    void createAnswer(@NonNull final String id) {
        getConnectionById(id).createAnswer(new BoyjSdpObserver(id), constraints);
    }

    void addLocalStream(@NonNull final String id,
                        @NonNull final MediaStream localMediaStream) {
        getConnectionById(id).addStream(localMediaStream);
    }

    void setLocalSdp(@NonNull final String id,
                     @NonNull SessionDescription sdp) {
        getConnectionById(id).setLocalDescription(new BoyjDefaultSdpObserver(id), sdp);
    }

    void setRemoteSdp(@NonNull final String id,
                      @NonNull final SessionDescription sdp) {
        getConnectionById(id).setRemoteDescription(new BoyjDefaultSdpObserver(id), sdp);
    }

    void addIceCandidate(@NonNull final String id,
                         @NonNull final IceCandidate candidate) {
        getConnectionById(id).addIceCandidate(candidate);
    }

    void dispose(@NonNull final String id) {
        //getConnectionById(id).dispose();
        connections.remove(id);
    }

    void disposeAll() {
        for (PeerConnection pc : connections.values()) {
            try {
                pc.dispose();
            } catch (Exception e) {

            }

        }
    }

    private PeerConnection getConnectionById(@NonNull final String id) {
        return connections.get(id);
    }

    private class BoyjPeerObserver extends BoyjPeerConnectionObserver {
        @NonNull
        private final String id;

        BoyjPeerObserver(@NonNull String id) {
            this.id = id;
        }

        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            final IceCandidatePayload payload = new IceCandidatePayload(iceCandidate);
            payload.setReceiver(id);
            iceCandidateSubject.onNext(payload);
        }

        @Override
        public void onAddStream(MediaStream mediaStream) {
            final BoyjMediaStream boyjMediaStream = new BoyjMediaStream(id, mediaStream);
            remoteMediaStreamSubject.onNext(boyjMediaStream);
        }
    }

    @NonNull
    Observable<IceCandidatePayload> iceCandidate() {
        return iceCandidateSubject.hide();
    }

    @NonNull
    Observable<BoyjMediaStream> remoteMediaStream() {
        return remoteMediaStreamSubject.hide();
    }

    private class BoyjSdpObserver extends BoyjDefaultSdpObserver {
        BoyjSdpObserver(@NonNull String id) {
            super(id);
        }

        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
            super.onCreateSuccess(sessionDescription);
            final SdpPayload payload = new SdpPayload(sessionDescription);
            payload.setReceiver(id);
            if (sessionDescription.type == SessionDescription.Type.OFFER) {
                offerSdpSubject.onNext(payload);
            } else if (sessionDescription.type == SessionDescription.Type.ANSWER) {
                answerSubject.onNext(payload);
            }
        }
    }

    @NonNull
    Observable<SdpPayload> offer() {
        return offerSdpSubject.hide();
    }

    @NonNull
    Observable<SdpPayload> answer() {
        return answerSubject.hide();
    }

    @NonNull
    public int getConnectionCount() {
        return connections.size();
    }
}