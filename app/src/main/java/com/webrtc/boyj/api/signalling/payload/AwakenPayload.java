package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import javax.annotation.Nonnull;

public class AwakenPayload {

    private String room;

    public void setRoom(@Nonnull String room) {
        this.room = room;
    }

    public static class Builder {

        private String room;

        public Builder(@NonNull final String room) {
            this.room = room;
        }

        @Nonnull
        public AwakenPayload build() {
            final AwakenPayload payload = new AwakenPayload();
            payload.setRoom(this.room);
            return payload;
        }
    }
}
