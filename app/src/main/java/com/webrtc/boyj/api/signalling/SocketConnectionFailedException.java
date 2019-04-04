package com.webrtc.boyj.api.signalling;


import android.support.annotation.NonNull;

public class SocketConnectionFailedException extends RuntimeException {

    @NonNull
    private static final String DEFAULT_MESSAGE = "socket connect failed";

    public SocketConnectionFailedException() {
        this(DEFAULT_MESSAGE);
    }

    public SocketConnectionFailedException(@NonNull final String message) {
        super(message);
    }

}
