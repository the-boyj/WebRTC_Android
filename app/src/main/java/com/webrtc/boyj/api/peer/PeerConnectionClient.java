package com.webrtc.boyj.api.peer;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.peer.manager.PeerConnectionFactoryManager;
import com.webrtc.boyj.utils.Logger;

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

    public PeerConnectionClient() {

    }

    private static final List<String> stunServerUrls = new ArrayList<>();
    private static final List<String> turnServerUrls = new ArrayList<>();
    @NonNull
    private static final List<PeerConnection.IceServer> iceServers = new ArrayList<>();
    @NonNull
    private static PeerConnection.RTCConfiguration rtcConfiguration;
    @NonNull
    private static PeerConnection.RTCConfiguration defaultRtcConfiguration;
    @NonNull
    private static final MediaConstraints constraints = new MediaConstraints();
    @NonNull
    private static final PeerConnectionFactory peerConnectionFactory;

    static {
        stunServerUrls.add("stun:tk-turn1.xirsys.com");
        turnServerUrls.add("turn:tk-turn1.xirsys.com:80?transport=udp");
        turnServerUrls.add("turn:tk-turn1.xirsys.com:3478?transport=udp");
        turnServerUrls.add("turn:tk-turn1.xirsys.com:80?transport=tcp");
        turnServerUrls.add("turn:tk-turn1.xirsys.com:3478?transport=tcp");
        turnServerUrls.add("turns:tk-turn1.xirsys.com:443?transport=tcp");
        turnServerUrls.add("turns:tk-turn1.xirsys.com:5349?transport=tcp");

        for (String stunServerUrl : stunServerUrls) {
            iceServers.add(
                    PeerConnection.IceServer.builder(stunServerUrl).createIceServer()
            );
        }

        for (String turnServerUrl : turnServerUrls) {
            iceServers.add(
                    PeerConnection.IceServer.builder(turnServerUrl)
                            .setUsername("aa1f1c54-39c4-11e9-9ab4-8a1138a37ce0")
                            .setPassword("aa1f1ccc-39c4-11e9-9fd9-42348e526b10")
                            .createIceServer()

            );
        }

        defaultRtcConfiguration = new PeerConnection.RTCConfiguration(iceServers);

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

    @SuppressWarnings("NullableProblems")
    @NonNull
    private PeerConnection peerConnection;
    @NonNull
    private PublishSubject<SessionDescription> sdpSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<IceCandidate> iceCandidateSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<MediaStream> remoteMediaStreamSubject = PublishSubject.create();

    public void createPeerConnection() {
        final PeerConnection peerConnection = peerConnectionFactory.createPeerConnection(rtcConfiguration, new BoyjPeerConnectionObserver());
        assert peerConnection != null;
        this.peerConnection = peerConnection;
    }

    public void createOffer() {
        peerConnection.createOffer(new BoyjSdpObserver(), constraints);
    }

    public void createAnswer() {
        peerConnection.createAnswer(new BoyjSdpObserver(), constraints);
    }

    public void setRemoteSdp(SessionDescription sdp) {
        Logger.d(sdp.type.canonicalForm());
        peerConnection.setRemoteDescription(new CustomSdpObserver("SDP"), sdp);
    }

    public void addIceCandidate(IceCandidate iceCandidate) {
        peerConnection.addIceCandidate(iceCandidate);
    }

    // Todo : 이후 Configuration 변경 대비
    public void setRtcConfiguration(PeerConnection.RTCConfiguration rtcConfiguration) {
        PeerConnectionClient.rtcConfiguration = rtcConfiguration;
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

    public void release() {
        peerConnection.dispose();
        peerConnectionFactory.dispose();
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
            Logger.d("onIceCandidate : " + iceCandidate.toString());
            iceCandidateSubject.onNext(iceCandidate);
        }

        @Override
        public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {

        }

        @Override
        public void onAddStream(MediaStream mediaStream) {
            Logger.d("onAddStream()");
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
