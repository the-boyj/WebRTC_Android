package com.webrtc.boyj.api.signalling;


import android.support.annotation.NonNull;

import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;

import javax.annotation.Nonnull;

public class SignalingClient {

    private final SocketIOClient socketIOClient;

    private static volatile SignalingClient instance;

    @Nonnull
    public static SignalingClient getInstance(@NonNull SocketIOClient socketIOClient) {

        socketIOClient.connect();

        if (instance == null) {
            synchronized (SignalingClient.class) {
                if (instance == null) {
                    instance = new SignalingClient(socketIOClient);
                }
            }
        }
        return instance;
    }

    private SignalingClient(@NonNull SocketIOClient socketIOClient) {
        this.socketIOClient = socketIOClient;
    }

    public void emitDial(@NonNull DialPayload dialPayload) {
        socketIOClient.getSocket().emit(SignalingEventString.EVENT_DIAL, dialPayload);
    }

    public void emitAwaken(@NonNull AwakenPayload awakenPayload) {
        socketIOClient.getSocket().emit(SignalingEventString.EVENT_AWAKEN, awakenPayload);
    }
}
