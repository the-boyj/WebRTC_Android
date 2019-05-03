package com.webrtc.boyj.api.boyjrtc.peer;

import androidx.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.PeerCallback;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.Participant;

import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import java.util.List;

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

    public void createOffers(@NonNull final List<Participant> participants,
                             @NonNull final MediaStream localMediaStream) {
        for (final Participant participant : participants) {
            createPeerConnection(participant.getUserId());
            createOffer(participant.getUserId());
            addLocalStream(participant.getUserId(), localMediaStream);
        }
    }

    public void createPeerConnection(@NonNull final String targetId) {
        boyjPeerConnection.createPeerConnection(targetId, peerConnectionFactory);
    }

    private void createOffer(@NonNull final String targetId) {
        boyjPeerConnection.createOffer(targetId);
    }

    public void createAnswer(@NonNull final String targetId) {
        boyjPeerConnection.createAnswer(targetId);
    }

    private void addLocalStream(@NonNull final String targetId,
                                @NonNull final MediaStream localStream) {
        boyjPeerConnection.addLocalStream(targetId, localStream);
    }

    public void setLocalSdp(@NonNull final String targetId,
                            @NonNull final SessionDescription sdp) {
        boyjPeerConnection.setLocalSdp(targetId, sdp);
    }

    public void setRemoteSdp(@NonNull final String targetId,
                             @NonNull final SessionDescription sdp) {
        boyjPeerConnection.setRemoteSdp(targetId, sdp);
    }

    public void addIceCandidate(@NonNull final String targetId,
                                @NonNull final IceCandidate iceCandidate) {
        boyjPeerConnection.addIceCandidate(targetId, iceCandidate);
    }

    public void dispose(@NonNull final String targetId) {
        boyjPeerConnection.dispose(targetId);
    }

    public void disposeAll() {
        boyjPeerConnection.disposeAll();
    }
}
