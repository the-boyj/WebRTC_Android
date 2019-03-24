package com.webrtc.boyj.api.peer;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.peer.manager.PeerConnectionFactoryManager;

import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
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
    private BoyjPeerConnection boyjPeerConnection;
    @NonNull
    private PublishSubject<SessionDescription> sdpSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<IceCandidate> iceCandidateSubject = PublishSubject.create();
    @NonNull
    private PublishSubject<MediaStream> remoteMediaStreamSubject = PublishSubject.create();

    public void createPeerConnection(boolean isCaller) {
        final PeerConnection peerConnection = peerConnectionFactory.createPeerConnection(rtcConfiguration, new PeerConnectionObserver());
        boyjPeerConnection = new BoyjPeerConnection(peerConnection);

        if (isCaller) {
            boyjPeerConnection.createOffer();
        }
    }

    public void setRtcConfiguration(PeerConnection.RTCConfiguration rtcConfiguration) {
        this.rtcConfiguration = rtcConfiguration;
    }

    public void release() {
        boyjPeerConnection.dispose();
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


    private class BoyjPeerConnection {
        @NonNull
        final private PeerConnection peer;

        public BoyjPeerConnection(PeerConnection peer) {
            this.peer = peer;
        }

        private void createOffer() {
            peer.createOffer(new LocalSdpObserver(), constraints);
        }

        private void createAnswer() {
            peer.createAnswer(new LocalSdpObserver(), constraints);
        }

        public void setRemoteDescription(SessionDescription sdp) {
            peer.setRemoteDescription(null, sdp);
        }

        public void addIceCandidate(IceCandidate iceCandidate) {
            peer.addIceCandidate(iceCandidate);
        }

        public void dispose() {
            peer.dispose();
        }
    }

    private class PeerConnectionObserver extends DefaultPeerConnectionObserver {
        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            iceCandidateSubject.onNext(iceCandidate);
        }

        @Override
        public void onAddStream(MediaStream mediaStream) {
            remoteMediaStreamSubject.onNext(mediaStream);
        }
    }

    private class LocalSdpObserver extends DefaultSdpObserver {
        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
            boyjPeerConnection.setRemoteDescription(sessionDescription);
            sdpSubject.onNext(sessionDescription);
        }
    }
}
