package com.webrtc.boyj.api.boyjrtc.peer;

import androidx.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.PeerCallback;
import com.webrtc.boyj.api.boyjrtc.peer.manager.RtcConfigurationManager;
import com.webrtc.boyj.api.boyjrtc.peer.observer.AnswerSdpObserver;
import com.webrtc.boyj.api.boyjrtc.peer.observer.BoyjPeerConnectionObserver;
import com.webrtc.boyj.api.boyjrtc.peer.observer.NoOpSdpObserver;
import com.webrtc.boyj.api.boyjrtc.peer.observer.OfferSdpObserver;

import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class BoyjPeerConnection {
    @NonNull
    private final MediaConstraints constraints = new MediaConstraints();
    @NonNull
    private final Map<String, PeerConnection> connections = new ConcurrentHashMap<>();
    @NonNull
    private final PeerCallback callback;

    BoyjPeerConnection(@NonNull final PeerCallback callback) {
        this.callback = callback;

        this.constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        this.constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
    }

    void createPeerConnection(@NonNull final String id,
                              @NonNull final PeerConnectionFactory factory) {
        final PeerConnection connection = factory.createPeerConnection(
                RtcConfigurationManager.createRtcConfiguration(),
                new BoyjPeerConnectionObserver(id, callback)
        );

        if (connection == null) {
            throw new IllegalStateException("PeerConnection is not created");
        }
        connections.put(id, connection);
    }

    void createOffer(@NonNull final String id) {
        getConnectionById(id).createOffer(new OfferSdpObserver(id, callback), constraints);
    }

    void createAnswer(@NonNull final String id) {
        getConnectionById(id).createAnswer(new AnswerSdpObserver(id, callback), constraints);
    }

    void addLocalStream(@NonNull final String id,
                        @NonNull final MediaStream localMediaStream) {
        getConnectionById(id).addStream(localMediaStream);
    }

    public void setLocalSdp(@NonNull final String id,
                            @NonNull SessionDescription sdp) {
        getConnectionById(id).setLocalDescription(new NoOpSdpObserver(id, callback), sdp);
    }

    void setRemoteSdp(@NonNull final String id,
                      @NonNull final SessionDescription sdp) {
        getConnectionById(id).setRemoteDescription(new NoOpSdpObserver(id, callback), sdp);
    }

    void addIceCandidate(@NonNull final String id,
                         @NonNull final IceCandidate candidate) {
        getConnectionById(id).addIceCandidate(candidate);
    }

    void dispose(@NonNull final String id) {
        getConnectionById(id).dispose();
        connections.remove(id);

        if (connections.isEmpty()) {
            callback.onCallFinish();
        }
    }

    void disposeAll() {
        for (PeerConnection pc : connections.values()) {
            pc.dispose();
        }
    }

    private PeerConnection getConnectionById(@NonNull final String id) {
        return connections.get(id);
    }
}