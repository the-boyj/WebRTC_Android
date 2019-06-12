package com.webrtc.boyj.api.boyjrtc.signalling;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.webrtc.boyj.BuildConfig;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.Payload;
import com.webrtc.boyj.utils.JSONUtil;
import com.webrtc.boyj.utils.Logger;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

class SocketIO {
    private static Socket socket;

    private SocketIO() {
        try {
            socket = IO.socket(BuildConfig.SERVER_URL);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("serverURL invalid." +
                    " check build.gradle variable : SERVER_URL ");
        }
    }

    public static SocketIO create() {
        return new SocketIO();
    }

    void connect() {
        socket.connect();
    }

    void disconnect() {
        socket.off();
        socket.disconnect();
    }

    void emit(@NonNull SocketEvent event,
              @NonNull final Payload payload) {
        socket.emit(event.toString(), JSONUtil.toJSONObject(payload));
    }

    void emit(@NonNull SocketEvent event,
              @Nullable final Object... args) {
        socket.emit(event.toString(), args);
    }

    void on(@NonNull SocketEvent event,
            @NonNull final Emitter.Listener fn) {
        socket.on(event.toString(), fn);
    }
}
