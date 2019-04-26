package com.webrtc.boyj.api.signalling;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.BuildConfig;
import com.webrtc.boyj.api.signalling.payload.Payload;
import com.webrtc.boyj.utils.JSONUtil;
import com.webrtc.boyj.utils.Logger;

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
        socket.connect();
    }

    void disconnect() {
        socket.off();
        socket.disconnect();
    }

    void emit(@NonNull SignalingEvent event,
              @NonNull final Payload payload) {
        Logger.i(payload.toString());
        socket.emit(event.toString(), JSONUtil.toJSONObject(payload));
    }

    void emit(@NonNull SignalingEvent event,
              @Nullable final Object... args) {
        Logger.i(event.toString());
        socket.emit(event.toString(), args);
    }

    void on(@NonNull SignalingEvent event,
            @NonNull final Emitter.Listener fn) {
        socket.on(event.toString(), fn);
    }
}
