package com.webrtc.boyj.api.boyjrtc.peer.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.webrtc.boyj.App;
import com.webrtc.boyj.api.boyjrtc.peer.IceServers;

import org.webrtc.PeerConnection;

import java.util.List;

public class RtcConfigurationManager {
    private static PeerConnection.RTCConfiguration rtcConfiguration;

    private static Integer convertIntegerOrNull(String str) {
        return "null".equals(str) ?
                null : Integer.parseInt(str);
    }

    private static Boolean convertBooleanOrNull(String str) {
        return "null".equals(str) ?
                null : Boolean.parseBoolean(str);
    }

    public static PeerConnection.RTCConfiguration createRtcConfiguration() {
        final SharedPreferences pref = App.getContext().getSharedPreferences("RtcConfiguration", Context.MODE_PRIVATE);

        final List<PeerConnection.IceServer> iceServerList = IceServers.getIceServerList();
        rtcConfiguration = new PeerConnection.RTCConfiguration(iceServerList);

        rtcConfiguration.iceRegatherIntervalRange = null;
        rtcConfiguration.cryptoOptions = null;
        rtcConfiguration.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        rtcConfiguration.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        rtcConfiguration.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfiguration.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        rtcConfiguration.keyType = PeerConnection.KeyType.ECDSA;
        rtcConfiguration.iceTransportsType = PeerConnection.IceTransportsType.valueOf(pref.getString("IceTransportsType", "ALL"));
        rtcConfiguration.bundlePolicy = PeerConnection.BundlePolicy.valueOf(pref.getString("BundlePolicy", "BALANCED"));
        rtcConfiguration.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.valueOf(pref.getString("RtcpMuxPolicy", "REQUIRE"));
        rtcConfiguration.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.valueOf(pref.getString("TcpCandidatePolicy", "ENABLED"));
        rtcConfiguration.candidateNetworkPolicy = PeerConnection.CandidateNetworkPolicy.valueOf(pref.getString("CandidateNetworkPolicy", "ALL"));
        rtcConfiguration.audioJitterBufferMaxPackets = Integer.parseInt(pref.getString("AudioJitterBufferMaxPackets", "50"));
        rtcConfiguration.audioJitterBufferFastAccelerate = pref.getBoolean("AudioJitterBufferFastAccelerate", false);
        rtcConfiguration.iceConnectionReceivingTimeout = Integer.parseInt(pref.getString("IceConnectionReceivingTimeout", "-1"));
        rtcConfiguration.iceBackupCandidatePairPingInterval = Integer.parseInt(pref.getString("IceBackupCandidatePairPingInterval", "-1"));
        rtcConfiguration.keyType = PeerConnection.KeyType.valueOf(pref.getString("KeyType", "ECDSA"));
        rtcConfiguration.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.valueOf(pref.getString("ContinualGatheringPolicy", "GATHER_ONCE"));
        rtcConfiguration.iceCandidatePoolSize = Integer.parseInt(pref.getString("IceCandidatePoolSize", "0"));
        rtcConfiguration.pruneTurnPorts = pref.getBoolean("PruneTurnPorts", false);
        rtcConfiguration.presumeWritableWhenFullyRelayed = pref.getBoolean("PresumeWritableWhenFullyRelayed", false);
        rtcConfiguration.iceCheckIntervalStrongConnectivityMs = convertIntegerOrNull(pref.getString("IceCheckIntervalStrongConnectivityMs", "null"));
        rtcConfiguration.iceCheckIntervalWeakConnectivityMs = convertIntegerOrNull(pref.getString("IceCheckIntervalWeakConnectivityMs", "null"));
        rtcConfiguration.iceCheckMinInterval = convertIntegerOrNull(pref.getString("IceCheckMinInterval", "null"));
        rtcConfiguration.iceUnwritableTimeMs = convertIntegerOrNull(pref.getString("IceUnwritableTimeMs", "null"));
        rtcConfiguration.iceUnwritableMinChecks = convertIntegerOrNull(pref.getString("IceUnwritableMinChecks", "null"));
        rtcConfiguration.stunCandidateKeepaliveIntervalMs = convertIntegerOrNull(pref.getString("StunCandidateKeepaliveIntervalMs", "null"));
        rtcConfiguration.disableIPv6OnWifi = pref.getBoolean("DisableIPv6OnWifi", false);
        rtcConfiguration.maxIPv6Networks = Integer.parseInt(pref.getString("MaxIPv6Networks", "5"));
        rtcConfiguration.disableIPv6OnWifi = pref.getBoolean("DisableIpv6", false);
        rtcConfiguration.enableDscp = pref.getBoolean("EnableDscp", false);
        rtcConfiguration.enableCpuOveruseDetection = pref.getBoolean("EnableCpuOveruseDetection", true);
        rtcConfiguration.enableRtpDataChannel = pref.getBoolean("EnableRtpDataChannel", false);
        rtcConfiguration.suspendBelowMinBitrate = pref.getBoolean("SuspendBelowMinBitrate", false);
        rtcConfiguration.screencastMinBitrate = convertIntegerOrNull(pref.getString("ScreencastMinBitrate", "null"));
        rtcConfiguration.combinedAudioVideoBwe = convertBooleanOrNull(pref.getString("CombinedAudioVideoBwe", "null"));
        rtcConfiguration.enableDtlsSrtp = convertBooleanOrNull(pref.getString("EnableDtlsSrtp", "null"));
        rtcConfiguration.networkPreference = PeerConnection.AdapterType.valueOf(pref.getString("AdapterType", "UNKNOWN"));
        rtcConfiguration.sdpSemantics = PeerConnection.SdpSemantics.valueOf(pref.getString("SdpSemantics", "PLAN_B"));
        rtcConfiguration.activeResetSrtpParams = pref.getBoolean("ActiveResetSrtpParams", false);
        rtcConfiguration.useMediaTransport = pref.getBoolean("UseMediaTransport", false);
        rtcConfiguration.useMediaTransportForDataChannels = pref.getBoolean("UseMediaTransportForDataChannels", false);

        return rtcConfiguration;
    }
}
