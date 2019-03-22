package com.webrtc.boyj.api.signalling;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.BuildConfig;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketIOClient {

    @NonNull
    private static final Socket socket;

    static {
        try {
            socket = IO.socket(BuildConfig.SERVER_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("serverURL invalid." +
                    " check build.gradle variable : SERVER_URL ");
        }
    }

    public void connect() {
        // 연결시도
        if (!socket.connected()) {
            socket.connect();

            //연결 실패시
            if (!socket.connected()) {
                throw new SocketConnectionFailedException();
            }
        }
    }

    public void disconnect() {
        socket.disconnect();
    }

    public void emit(@NonNull final String event, @Nullable final Object... args) {
        socket.emit(event, args);
    }
}
