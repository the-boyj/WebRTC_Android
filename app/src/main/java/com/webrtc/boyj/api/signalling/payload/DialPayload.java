package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import javax.annotation.Nonnull;

public class DialPayload {

    private String deviceToken;

    public void setDeviceToken(@Nonnull String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public static class Builder {

        private String deviceToken;

        public Builder(@NonNull final String deviceToken) {
            this.deviceToken = deviceToken;
        }

        @Nonnull
        public DialPayload build() {
            final DialPayload payload = new DialPayload();
            payload.setDeviceToken(this.deviceToken);
            return payload;
        }
    }
}
