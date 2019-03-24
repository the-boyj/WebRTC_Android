package com.webrtc.boyj.api.peer;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.RtpReceiver;


public abstract class DefaultPeerConnectionObserver implements PeerConnection.Observer {
    @Override
    public abstract void onIceCandidate(IceCandidate iceCandidate);

    @Override
    public abstract void onAddStream(MediaStream mediaStream);

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
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
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
