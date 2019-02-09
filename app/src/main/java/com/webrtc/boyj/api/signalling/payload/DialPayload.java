package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

public class DialPayload {
    private String deviceToken;

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public static class Builder {
        private String deviceToken;

        public Builder(@NonNull final String deviceToken) {
            this.deviceToken = deviceToken;
        }
        
        public DialPayload build() {
            final DialPayload payload = new DialPayload();
            payload.setDeviceToken(this.deviceToken);
            return payload;
        }
    }

}
