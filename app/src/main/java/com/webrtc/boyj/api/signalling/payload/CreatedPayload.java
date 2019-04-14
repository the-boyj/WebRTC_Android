package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.webrtc.boyj.utils.JSONUtil;

public class CreatedPayload {
    @NonNull
    private final String calleeId;

    private CreatedPayload(@NonNull final String calleeId) {
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

        public CreatedPayload build() {
            return new CreatedPayload(calleeId);
        }
    }
}
