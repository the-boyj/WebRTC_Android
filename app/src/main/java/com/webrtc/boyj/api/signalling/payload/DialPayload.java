package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.webrtc.boyj.utils.JSONUtil;


public class DialPayload {
    @NonNull
    private final String calleeId; // Caller가 전화 송신시 상대방 번호

    private DialPayload(@NonNull String calleeId) {
        this.calleeId = calleeId;
    }

    @NonNull
    public String getCalleeId() {
        return calleeId;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONUtil.toJson(getClass(), this);
    }

    public static class Builder {
        @NonNull
        private final String calleeId;

        public Builder(@NonNull final String calleeId) {
            this.calleeId = calleeId;
        }

        @NonNull
        public DialPayload build() {
            return new DialPayload(calleeId);
        }
    }
}
