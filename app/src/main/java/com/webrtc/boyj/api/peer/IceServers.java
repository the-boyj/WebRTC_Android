package com.webrtc.boyj.api.peer;

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
                Arrays.asList("stun:tk-turn1.xirsys.com")
        );
        final List<String> turnUrlList = new ArrayList<>(
                Arrays.asList(
                        "turn:tk-turn1.xirsys.com:80?transport=udp",
                        "turn:tk-turn1.xirsys.com:3478?transport=udp",
                        "turn:tk-turn1.xirsys.com:80?transport=tcp",
                        "turn:tk-turn1.xirsys.com:3478?transport=tcp",
                        "turns:tk-turn1.xirsys.com:443?transport=tcp",
                        "turns:tk-turn1.xirsys.com:5349?transport=tcp"
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
                            .setUsername("aa1f1c54-39c4-11e9-9ab4-8a1138a37ce0")
                            .setPassword("aa1f1ccc-39c4-11e9-9fd9-42348e526b10")
                            .createIceServer()
            );
        }

        return iceServerList;
    }
}
