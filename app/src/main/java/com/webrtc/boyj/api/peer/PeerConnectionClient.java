package com.webrtc.boyj.api.peer;

import android.support.annotation.NonNull;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

public class PeerConnectionClient {
    @NonNull
    private final List<PeerConnection.IceServer> iceServerList;
    @NonNull
    private final PeerConnection.RTCConfiguration rtcConfiguration;
    @NonNull
    private final MediaConstraints constraints = new MediaConstraints();
    @NonNull
    private final PeerConnectionFactory peerConnectionFactory;
    @NonNull
    private PeerConnection peerConnection;
    @NonNull
    private PublishSubject<SessionDescription> sdpSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<IceCandidate> iceCandidateSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<MediaStream> remoteMediaStreamSubject = PublishSubject.create();

    public PeerConnectionClient(@NonNull final PeerConnectionFactory peerConnectionFactory) {
        this.peerConnectionFactory = peerConnectionFactory;
        this.iceServerList = IceServers.getIceServerList();
        this.rtcConfiguration = createRtcConfiguration();


        this.constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        this.constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
    }

    private PeerConnection.RTCConfiguration createRtcConfiguration() {
        final PeerConnection.RTCConfiguration rtcConfiguration = new PeerConnection.RTCConfiguration(iceServerList);

        rtcConfiguration.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        rtcConfiguration.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        rtcConfiguration.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfiguration.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        rtcConfiguration.keyType = PeerConnection.KeyType.ECDSA;

        return rtcConfiguration;
    }

    public void createPeerConnection() {
        final PeerConnection peerConnection = peerConnectionFactory.createPeerConnection(rtcConfiguration, new BoyjPeerConnectionObserver());
        if (peerConnection == null) {
            throw new IllegalStateException("Peer connection has invalid status");
        }
        this.peerConnection = peerConnection;
    }

    public void createOffer() {
        peerConnection.createOffer(new BoyjSdpObserver(), constraints);
    }

    public void createAnswer() {
        peerConnection.createAnswer(new BoyjSdpObserver(), constraints);
    }

    public void setRemoteSdp(@NonNull final SessionDescription sdp) {
        peerConnection.setRemoteDescription(new CustomSdpObserver("SDP"), sdp);
    }

    public void addIceCandidate(@NonNull final IceCandidate iceCandidate) {
        peerConnection.addIceCandidate(iceCandidate);
    }

    public void addStreamToLocalPeer(@NonNull final MediaStream userMedia) {
        peerConnection.addStream(userMedia);
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

    public void dispose() {
        peerConnection.dispose();
    }

    private class BoyjPeerConnectionObserver implements PeerConnection.Observer {

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
            iceCandidateSubject.onNext(iceCandidate);
        }

        @Override
        public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {

        }

        @Override
        public void onAddStream(MediaStream mediaStream) {
            remoteMediaStreamSubject.onNext(mediaStream);
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

    private class BoyjSdpObserver implements SdpObserver {

        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
            peerConnection.setLocalDescription(new CustomSdpObserver("AAA"), sessionDescription);
            sdpSubject.onNext(sessionDescription);
        }

        @Override
        public void onSetSuccess() {

        }

        @Override
        public void onCreateFailure(String s) {
        }

        @Override
        public void onSetFailure(String s) {

        }
    }
}
