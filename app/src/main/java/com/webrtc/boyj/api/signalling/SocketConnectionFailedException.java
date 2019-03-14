package com.webrtc.boyj.api.signalling;


public class SocketConnectionFailedException extends RuntimeException {

    private static final String message = "socket connect failed";

    public SocketConnectionFailedException() {
        super(message);
    }

    public SocketConnectionFailedException(String message) {
        super(message);
    }

}
