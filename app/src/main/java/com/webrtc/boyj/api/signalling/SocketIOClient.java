package com.webrtc.boyj.api.signalling;


import android.support.annotation.NonNull;

import io.socket.client.Socket;

public class SocketIOClient {

    final private Socket socket;

    public SocketIOClient(@NonNull Socket socket) {
        this.socket = socket;
    }

    public void connect() {
        socket.connect();
        if (!socket.connected()) {
            throw new SocketConnectionFailedException();
        }
    }

    @NonNull
    public Socket getSocket() {
        return socket;
    }
}
