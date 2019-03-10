package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;


public class AwakenPayload {

    private String room;

    public void setRoom(@NonNull String room) {
        this.room = room;
    }

    public static class Builder {

        private String room;

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
