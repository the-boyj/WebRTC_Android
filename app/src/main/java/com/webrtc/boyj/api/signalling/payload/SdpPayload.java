package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import org.webrtc.SessionDescription;

public class SdpPayload {
    @NonNull
    private SessionDescription sdp;

    public void setSdp(@NonNull final SessionDescription sdp) {
        this.sdp = sdp;
    }

    public static class Builder {
        @NonNull
        private final SessionDescription sdp;

        public Builder(@NonNull final SessionDescription sdp) {
            this.sdp = sdp;
        }

        @NonNull
        public SdpPayload build() {
            final SdpPayload sdpPayload = new SdpPayload();
            sdpPayload.setSdp(this.sdp);
            return sdpPayload;
        }
    }


}



