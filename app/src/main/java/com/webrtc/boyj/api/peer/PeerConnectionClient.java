package com.webrtc.boyj.api.peer;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.peer.manager.PeerConnectionFactoryManager;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

public class PeerConnectionClient {

    @NonNull
    private static final List<PeerConnection.IceServer> iceServers = new ArrayList<>();

    @NonNull
    private static PeerConnection.RTCConfiguration rtcConfiguration;
    @NonNull
    private static final PeerConnection.RTCConfiguration defaultRtcConfiguration = new PeerConnection.RTCConfiguration(iceServers);

    @NonNull
    private static final MediaConstraints constraints = new MediaConstraints();

    @NonNull
    private static final PeerConnectionFactory peerConnectionFactory;


    static {
        /*
        RTC Configuration:
            https://developer.mozilla.org/en-US/docs/Web/API/RTCConfiguration
            http://cocoadocs.org/docsets/GoogleWebRTC/1.1.20266/Classes/RTCConfiguration.html#//api/name/keyType
         */
        defaultRtcConfiguration.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        defaultRtcConfiguration.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        defaultRtcConfiguration.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        defaultRtcConfiguration.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        defaultRtcConfiguration.keyType = PeerConnection.KeyType.ECDSA;
        rtcConfiguration = defaultRtcConfiguration;

        constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
        peerConnectionFactory = PeerConnectionFactoryManager.getPeerConnectionFactory();
    }

    @NonNull
    private PeerConnection peerConnection;
    @NonNull
    private PublishSubject<SessionDescription> sdpSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<IceCandidate> iceCandidateSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<MediaStream> remoteMediaStreamSubject = PublishSubject.create();

    public void createPeerConnection(boolean isCaller) {
        final PeerConnection peerConnection = peerConnectionFactory.createPeerConnection(rtcConfiguration, new BoyjPeerConnectionObserver());

        if (isCaller) {
            peerConnection.createOffer(new BoyjSdpObserver(), constraints);
        }
    }

    public void setRtcConfiguration(PeerConnection.RTCConfiguration rtcConfiguration) {
        this.rtcConfiguration = rtcConfiguration;
    }

    public void release() {
        peerConnection.dispose();
        peerConnectionFactory.dispose();
    }

    public PublishSubject<SessionDescription> getSdpSubject() {
        return sdpSubject;
    }

    public PublishSubject<IceCandidate> getIceCandidateSubject() {
        return iceCandidateSubject;
    }

    public PublishSubject<MediaStream> getRemoteMediaStreamSubject() {
        return remoteMediaStreamSubject;
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
            peerConnection.setRemoteDescription(null, sessionDescription);
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
