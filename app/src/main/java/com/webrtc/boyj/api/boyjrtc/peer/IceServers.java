package com.webrtc.boyj.api.boyjrtc.peer;

import androidx.annotation.NonNull;

import org.webrtc.PeerConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IceServers {

    @NonNull
    private static final String CREDENTIAL_ID = "MSoK3Kvm1-C2f9k69-CLx3C9l2QierkJ-XVorFNGLQpfEt8akAIoQgYS_fACs9MtAAAAAFz_FdRiaHcxOTk0";
    @NonNull
    private static final String CREDENTIAL_PWD = "ffec241c-8bf2-11e9-b318-066b071c7196";

    @NonNull
    private static final List<String> stunUrlList = new ArrayList<>(
            Arrays.asList("stun:tk-turn2.xirsys.com")
    );

    @NonNull
    private static final List<String> turnUrlList = new ArrayList<>(
            Arrays.asList(
                    "turn:tk-turn2.xirsys.com:80?transport=udp",
                    "turn:tk-turn2.xirsys.com:3478?transport=udp",
                    "turn:tk-turn2.xirsys.com:80?transport=tcp",
                    "turn:tk-turn2.xirsys.com:3478?transport=tcp",
                    "turns:tk-turn2.xirsys.com:443?transport=tcp",
                    "turns:tk-turn2.xirsys.com:5349?transport=tcp"
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
        return iceServerList;
    }
}
