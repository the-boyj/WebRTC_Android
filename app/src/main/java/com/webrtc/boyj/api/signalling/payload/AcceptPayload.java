package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.webrtc.boyj.utils.JSONUtil;

import org.webrtc.SessionDescription;

public class AcceptPayload {
    @NonNull
    private final SessionDescription sdp;

    private AcceptPayload(@NonNull SessionDescription sdp) {
        this.sdp = sdp;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONUtil.toJson(getClass(), this);
    }

    public static class Builder {
        @NonNull
        private final SessionDescription sdp;

        public Builder(@NonNull SessionDescription sdp) {
            this.sdp = sdp;
        }

        public AcceptPayload build() {
            return new AcceptPayload(sdp);
        }
    }
}
