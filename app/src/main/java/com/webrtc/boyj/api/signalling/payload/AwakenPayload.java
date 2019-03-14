package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;


public class AwakenPayload {
    @NonNull
    private String room;

    public void setRoom(@NonNull final String room) {
        this.room = room;
    }

    public static class Builder {


        @NonNull
        private final String room;

        public Builder(@NonNull final String room) {
            this.room = room;
        }

        @NonNull
        public AwakenPayload build() {
            final AwakenPayload payload = new AwakenPayload();
            payload.setRoom(this.room);
            return payload;
        }
    }
}
