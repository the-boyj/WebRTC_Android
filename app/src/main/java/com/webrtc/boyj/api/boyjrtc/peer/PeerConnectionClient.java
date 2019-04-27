package com.webrtc.boyj.api.boyjrtc.peer;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.PeerCallback;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.Participant;

import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import java.util.List;

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

    public void createOffers(@NonNull final List<Participant> participants,
                             @NonNull final MediaStream localMediaStream) {
        for (final Participant participant : participants) {
            createOffer(participant.getUserId(), localMediaStream);
        }
    }

    private void createOffer(@NonNull final String targetId,
                             @NonNull final MediaStream localMediaStream) {
        initPeerConnection(targetId, localMediaStream);
        boyjPeerConnection.createOffer(targetId);
    }

    public void createAnswer(@NonNull final String targetId,
                             @NonNull final MediaStream localMediaStream) {
        initPeerConnection(targetId, localMediaStream);
        boyjPeerConnection.createAnswer(targetId);
    }

    private void initPeerConnection(@NonNull final String targetId,
                                    @NonNull final MediaStream localMediaStream) {
        boyjPeerConnection.createPeerConnection(targetId, peerConnectionFactory);
        boyjPeerConnection.addLocalStream(targetId, localMediaStream);
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
