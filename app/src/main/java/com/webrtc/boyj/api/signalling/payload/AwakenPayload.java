package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

public class AwakenPayload {
    private String room;

    public void setRoom(String room) {
        this.room = room;
    }

    public static class Builder {
        private String room;

        public Builder(@NonNull final String room) {
            this.room = room;
        }

        public AwakenPayload build() {
            AwakenPayload awakenPayload =new AwakenPayload();
            awakenPayload.setRoom(this.room);
            return awakenPayload;
        }
    }

}
