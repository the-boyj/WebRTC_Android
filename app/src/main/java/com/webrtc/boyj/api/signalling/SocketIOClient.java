package com.webrtc.boyj.api.signalling;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.BuildConfig;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

class SocketIOClient {
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

    SocketIOClient() {

    }

    void connect() {
        if (!socket.connected()) {
            socket.connect();
        }

    }

    void disconnect() {
        socket.off();
        socket.disconnect();
    }

    void emit(@NonNull @SignalingClient.Event String event,
              @Nullable final Object... args) {
        socket.emit(event, args);
    }

    void on(@NonNull @SignalingClient.Event String event,
            @NonNull final Emitter.Listener fn) {
        socket.on(event, fn);
    }
}
