package com.webrtc.boyj.api.peer.observer;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.peer.PeerCallback;
import com.webrtc.boyj.api.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.data.model.BoyjMediaStream;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.RtpReceiver;

public class BoyjPeerConnectionObserver implements PeerConnection.Observer {
    @NonNull
    private final String id;
    @NonNull
    private final PeerCallback callback;

    public BoyjPeerConnectionObserver(@NonNull final String id,
                                      @NonNull final PeerCallback callback) {
        this.id = id;
        this.callback = callback;
    }

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {

    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {

    }

    @Override
    public void onIceConnectionReceivingChange(boolean b) {

    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {

    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        final IceCandidatePayload payload = new IceCandidatePayload(iceCandidate);
        payload.setReceiver(id);
        callback.onIceCandidatePayloadFromPeer(payload);
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {

    }

    @Override
    public void onAddStream(MediaStream mediaStream) {
        final BoyjMediaStream boyjMediaStream = new BoyjMediaStream(id, mediaStream);
        callback.onRemoteStreamFromPeer(boyjMediaStream);
    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {

    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {

    }

    @Override
    public void onRenegotiationNeeded() {

    }

    @Override
    public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {

    }
}
