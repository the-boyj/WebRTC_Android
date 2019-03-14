package com.webrtc.boyj.api.signalling;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.BuildConfig;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SignalingClientFactory {

    @Nullable
    private static Socket socket;

    //for test
    @NonNull
    public static SignalingClient getSignalingClient(@NonNull final SocketIOClient socketIOClient) {
        return SignalingClient.getInstance(socketIOClient);
    }

    @NonNull
    public static SignalingClient getSignalingClient() {
        if (socket == null) {
            try {
                socket = IO.socket(BuildConfig.SERVER_URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        final SocketIOClient socketIOClient = new SocketIOClient(socket);
        final SignalingClient signalingClient = getSignalingClient(socketIOClient);

        return signalingClient;
    }
}
