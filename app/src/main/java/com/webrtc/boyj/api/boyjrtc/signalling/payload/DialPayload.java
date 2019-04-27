package com.webrtc.boyj.api.boyjrtc.signalling.payload;

import android.support.annotation.NonNull;

public class DialPayload extends Payload {
    @NonNull
    private final String calleeId; // Caller가 전화 송신시 상대방 번호

    public DialPayload(@NonNull String calleeId) {
        this.calleeId = calleeId;
    }
}
