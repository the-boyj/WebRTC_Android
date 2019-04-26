package com.webrtc.boyj.api.peer;

import android.support.annotation.NonNull;

import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

@SuppressWarnings("SpellCheckingInspection")
public class PeerConnectionClient {
    @NonNull
    private final PeerConnectionFactory peerConnectionFactory;
    @NonNull
    private final BoyjPeerConnection boyjPeerConnection;

    public PeerConnectionClient(@NonNull final PeerConnectionFactory peerConnectionFactory,
                                @NonNull final PeerCallback callback) {
        this.peerConnectionFactory = peerConnectionFactory;
        this.boyjPeerConnection = new BoyjPeerConnection(callback);
    }

    public void createPeerConnection(@NonNull final String targetId) {
        boyjPeerConnection.createPeerConnection(targetId, peerConnectionFactory);
    }

    public void createOffer(@NonNull final String targetId) {
        boyjPeerConnection.createOffer(targetId);
    }

    public void connectOffer(@NonNull final String targetId) {
        boyjPeerConnection.connectOffer(targetId);
    }

    public void createAnswer(@NonNull final String targetId) {
        boyjPeerConnection.createAnswer(targetId);
    }

    public void addLocalStream(@NonNull final String targetId,
                               @NonNull final MediaStream localMediaStream) {
        boyjPeerConnection.addLocalStream(targetId, localMediaStream);
    }

    public void setRemoteSdp(@NonNull final String targetId,
                             @NonNull final SessionDescription sdp) {
        boyjPeerConnection.setRemoteSdp(targetId, sdp);
    }

    public void addIceCandidate(@NonNull final String targetId,
                                @NonNull final IceCandidate iceCandidate) {
        boyjPeerConnection.addIceCandidate(targetId, iceCandidate);
    }

    public boolean isConnectedById(@NonNull final String id) {
        return boyjPeerConnection.getConnectionById(id) != null;
    }

    public void dispose(@NonNull final String targetId) {
        boyjPeerConnection.dispose(targetId);
    }

    public void disposeAll() {
        boyjPeerConnection.disposeAll();
    }
}
