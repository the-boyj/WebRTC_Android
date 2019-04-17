package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.webrtc.boyj.utils.JSONUtil;

public class AwakenPayload {
    @NonNull
    private final String room;
    @NonNull
    private final String callerId;
    @NonNull
    private final String calleeId;

    private AwakenPayload(@NonNull final String room,
                          @NonNull final String callerId,
                          @NonNull final String calleeId) {
        this.room = room;
        this.callerId = callerId;
        this.calleeId = calleeId;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONUtil.toJson(getClass(), this);
    }

    public static class Builder {
        @NonNull
        private final String room;
        @NonNull
        private final String callerId;
        @NonNull
        private final String calleeId;

        public Builder(@NonNull final String room,
                       @NonNull final String callerId,
                       @NonNull final String calleeId) {
            this.room = room;
            this.callerId = callerId;
            this.calleeId = calleeId;
        }

        @NonNull
        public AwakenPayload build() {
            return new AwakenPayload(room, callerId, calleeId);
        }
    }
}
