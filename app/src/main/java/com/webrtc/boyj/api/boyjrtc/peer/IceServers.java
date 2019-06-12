package com.webrtc.boyj.api.boyjrtc.peer;

import androidx.annotation.NonNull;

import org.webrtc.PeerConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IceServers {

    @NonNull
    private static final String CREDENTIAL_ID = "boyj";
    @NonNull
    private static final String CREDENTIAL_PWD = "boyj";

    private static final String PRIVATE_ID = "aa1f1c54-39c4-11e9-9ab4-8a1138a37ce0";
    private static final String PRIVATE_CREDENCIAL= "aa1f1ccc-39c4-11e9-9fd9-42348e526b10";

    private static final List<String> private_serverUrlList = new ArrayList<>(
            Arrays.asList(
                    "turn:tk-turn1.xirsys.com:80?transport=udp",
                    "turn:tk-turn1.xirsys.com:3478?transport=udp",
                    "turn:tk-turn1.xirsys.com:80?transport=tcp",
                    "turn:tk-turn1.xirsys.com:3478?transport=tcp",
                    "turns:tk-turn1.xirsys.com:443?transport=tcp",
                    "turns:tk-turn1.xirsys.com:5349?transport=tcp"
            )
    );


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

        for (final String url : stunUrlList) {
            iceServerList.add(
                    PeerConnection.IceServer.builder(url).createIceServer()
            );
        }

        for (final String url : turnUrlList) {
            iceServerList.add(
                    PeerConnection.IceServer.builder(url)
                            .setUsername(CREDENTIAL_ID)
                            .setPassword(CREDENTIAL_PWD)
                            .createIceServer()
            );
        }

        for (final String url : private_serverUrlList) {
            iceServerList.add(
                    PeerConnection.IceServer.builder(url)
                            .setUsername(PRIVATE_ID)
                            .setPassword(PRIVATE_CREDENCIAL)
                            .createIceServer()
            );
        }
        return iceServerList;
    }
}
