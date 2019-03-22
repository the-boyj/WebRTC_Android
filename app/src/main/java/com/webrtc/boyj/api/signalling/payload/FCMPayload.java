package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;


public class FCMPayload extends Payload {

    private static final String KEY_ROOM = "room";
    private static final String KEY_TEL = "tel";

    @NonNull
    private final String room;
    @NonNull
    private final String tel;

    public FCMPayload(@NonNull final RemoteMessage remoteMessage) {
        this.room = remoteMessage.getData().get(KEY_ROOM);
        this.tel = remoteMessage.getData().get(KEY_TEL);
    }

    @NonNull
    public String getRoom() {
        return room;
    }

    @NonNull
    public String getTel() {
        return tel;
    }
}
