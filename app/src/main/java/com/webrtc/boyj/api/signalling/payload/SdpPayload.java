package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.webrtc.SessionDescription;

public class SdpPayload extends Payload {
    @NonNull
    private SessionDescription sdp;

    public void setSdp(@NonNull final SessionDescription sdp) {
        this.sdp = sdp;
    }

    @NonNull
    public static SdpPayload fromJson(@NonNull final String jsonString) {
        return new Gson().fromJson(jsonString, SdpPayload.class);
    }


    @NonNull
    public SessionDescription getSdp() {
        return sdp;
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



