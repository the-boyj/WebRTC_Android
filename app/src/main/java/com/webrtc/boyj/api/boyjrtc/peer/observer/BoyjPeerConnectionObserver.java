package com.webrtc.boyj.api.boyjrtc.peer.observer;

import android.util.Log;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.RtpReceiver;

import java.util.ArrayList;
import java.util.List;

public class BoyjPeerConnectionObserver implements PeerConnection.Observer {
    List<IceCandidate> candidates = new ArrayList<>();

    @Override
    public void onConnectionChange(PeerConnection.PeerConnectionState newState) {
        Log.d("bhwState", "cState : " + newState.toString());
    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
        Log.d("bhwState", "iState : " + iceConnectionState.toString());
    }

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {
        Log.d("bhwTest", signalingState.toString());
    }


    @Override
    public void onIceConnectionReceivingChange(boolean b) {
        Log.d("bhwTest", Boolean.toString(b));
    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
        Log.d("bhwTest", iceGatheringState.toString());
    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {

    }

    @Override
    public void onAddStream(MediaStream mediaStream) {

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
