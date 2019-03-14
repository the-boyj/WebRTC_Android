package com.webrtc.boyj.api.signalling;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;

public class SignalingClient {

    @NonNull
    private final SocketIOClient socketIOClient;


    @Nullable
    private static volatile SignalingClient instance;

    @NonNull
    public static SignalingClient getInstance(@NonNull final SocketIOClient socketIOClient) {

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

    private SignalingClient(@NonNull final SocketIOClient socketIOClient) {
        this.socketIOClient = socketIOClient;
    }

    public void emitDial(@NonNull final DialPayload dialPayload) {
        socketIOClient.getSocket().emit(SignalingEventString.EVENT_DIAL, dialPayload);
    }

    public void emitAwaken(@NonNull final AwakenPayload awakenPayload) {
        socketIOClient.getSocket().emit(SignalingEventString.EVENT_AWAKEN, awakenPayload);
    }
}
