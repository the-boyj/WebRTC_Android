package com.webrtc.boyj.api.peer.manager;

import com.webrtc.boyj.api.peer.IceServers;

import org.webrtc.PeerConnection;

import java.util.List;

public class RtcConfigurationManager {
    private static PeerConnection.RTCConfiguration rtcConfiguration;

    public static PeerConnection.RTCConfiguration createRtcConfiguration() {
        final List<PeerConnection.IceServer> iceServerList = IceServers.getIceServerList();
        rtcConfiguration = new PeerConnection.RTCConfiguration(iceServerList);

        rtcConfiguration.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        rtcConfiguration.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        rtcConfiguration.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfiguration.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        rtcConfiguration.keyType = PeerConnection.KeyType.ECDSA;

        return rtcConfiguration;
    }
}
