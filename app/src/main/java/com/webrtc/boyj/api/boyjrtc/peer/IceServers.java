package com.webrtc.boyj.api.boyjrtc.peer;

import org.webrtc.PeerConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IceServers {
    private IceServers() {

    }

    public static List<PeerConnection.IceServer> getIceServerList() {
        final List<PeerConnection.IceServer> iceServerList = new ArrayList<>();
        final List<String> stunUrlList = new ArrayList<>(
                Arrays.asList(
                        "stun:15.164.123.209:3478",
                        "stun:15.164.65.225:3478",
                        "stun:52.78.33.97:3478"
                )
        );

        final String turnServeruserName = "boyj";
        final String turnServerCredential = "boyj";
        final List<String> turnUrlList = new ArrayList<>(
                Arrays.asList(
                        "turn:15.164.123.209:3478",
                        "turn:15.164.65.225:3478",
                        "turn:52.78.33.97:3478"
                )
        );


        for (final String stunServerUrl : stunUrlList) {
            iceServerList.add(
                    PeerConnection.IceServer.builder(stunServerUrl)
                            .createIceServer()
            );
        }

        for (final String turnServerUrl : turnUrlList) {
            iceServerList.add(
                    PeerConnection.IceServer.builder(turnServerUrl)
                            .setUsername(turnServeruserName)
                            .setPassword(turnServerCredential)
                            .createIceServer()
            );
        }

        return iceServerList;
    }
}
