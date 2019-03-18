package com.webrtc.boyj.api.signalling;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.socket.client.Socket;

public class SocketIOClient {

    @NonNull
    final private Socket socket;

    public SocketIOClient(@NonNull final Socket socket) {
        this.socket = socket;
    }

    public void connect() {
        socket.connect();
        if (!socket.connected()) {
            throw new SocketConnectionFailedException();
        }
    }

    @NonNull
    public void emit(@NonNull final String event , @Nullable final Object object){
        socket.emit(event , object);
    }
    @NonNull
    public Socket getSocket() {
        return socket;
    }
}
