package com.webrtc.boyj.api.boyjrtc.peer;

import androidx.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.BoyjMediaStream;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.SdpPayload;

import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class PeerConnectionClient {
    @NonNull
    private final PeerConnectionFactory peerConnectionFactory;
    @NonNull
    private final BoyjPeerConnection boyjPeerConnection;

    public static PeerConnectionClient of(@NonNull final PeerConnectionFactory factory) {
        return new PeerConnectionClient(factory);
    }

    private PeerConnectionClient(@NonNull final PeerConnectionFactory peerConnectionFactory) {
        this.peerConnectionFactory = peerConnectionFactory;
        this.boyjPeerConnection = new BoyjPeerConnection();
    }

    public void createPeerConnection(@NonNull final String targetId) {
        boyjPeerConnection.createPeerConnection(targetId, peerConnectionFactory);
    }

    public void createOffer(@NonNull final String targetId) {
        boyjPeerConnection.createOffer(targetId);
    }

    public void createAnswer(@NonNull final String targetId) {
        boyjPeerConnection.createAnswer(targetId);
    }

    public void addLocalStream(@NonNull final String targetId,
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

    @NonNull
    public Observable<IceCandidatePayload> iceCandidate() {
        return boyjPeerConnection.iceCandidate();
    }

    @NonNull
    public Observable<BoyjMediaStream> remoteMediaStream() {
        return boyjPeerConnection.remoteMediaStream();
    }

    @NonNull
    public Observable<SdpPayload> offer() {
        return boyjPeerConnection.offer();
    }

    @NonNull
    public Observable<SdpPayload> answer() {
        return boyjPeerConnection.answer();
    }

    public int getConnectionCount() {
        return boyjPeerConnection.getConnectionCount();
    }

    @NonNull
    public PublishSubject<String> connectionStateSubject() {
        return boyjPeerConnection.connectionStateSubject();
    }

    public void removeConnection(String id) {
        boyjPeerConnection.removeConnection(id);
    }

    public boolean isConnected(String id) {
        return boyjPeerConnection.isConnected(id);
    }

    public Set<String> peersId() {
        return boyjPeerConnection.getPeersId();
    }
}
