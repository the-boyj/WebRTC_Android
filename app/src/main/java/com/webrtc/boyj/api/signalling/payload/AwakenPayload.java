package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

public class AwakenPayload extends Payload {
    @NonNull
    private final String room;
    @NonNull
    private final String callerId;
    @NonNull
    private final String calleeId;

    public AwakenPayload(@NonNull final String room,
                         @NonNull final String callerId,
                         @NonNull final String calleeId) {
        this.room = room;
        this.callerId = callerId;
        this.calleeId = calleeId;
    }
}
