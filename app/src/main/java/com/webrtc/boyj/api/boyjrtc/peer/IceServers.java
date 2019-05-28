package com.webrtc.boyj.api.boyjrtc.peer;

import org.webrtc.PeerConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IceServers {

    private static final String CREDENTIAL_ID = "boyj";
    private static final String CREDENTIAL_PWD = "boyj";

    private static final List<String> serverUrlList = new ArrayList<>(
            Arrays.asList(
                    "turn:13.124.30.187:3478",
                    "turn:13.209.192.5:3478"
            )
    );

    private IceServers() {

    }

    public static List<PeerConnection.IceServer> getIceServerList() {
        final List<PeerConnection.IceServer> iceServerList = new ArrayList<>();

        for (final String url : serverUrlList) {
            iceServerList.add(
                    PeerConnection.IceServer.builder(url)
                            .setUsername(CREDENTIAL_ID)
                            .setPassword(CREDENTIAL_PWD)
                            .createIceServer()
            );
        }

        return iceServerList;
    }
}
