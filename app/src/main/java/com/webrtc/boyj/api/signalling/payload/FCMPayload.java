package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;

import javax.annotation.Nonnull;

public class FCMPayload {

    private static final String KEY_ROOM = "room";
    private static final String KEY_TEL = "tel";

    @NonNull private final String room;
    @NonNull private final String tel;

    public FCMPayload(@NonNull RemoteMessage remoteMessage) {
        this.room = remoteMessage.getData().get(KEY_ROOM);
        this.tel = remoteMessage.getData().get(KEY_TEL);
    }

    @Nonnull
    public String getRoom() {
        return room;
    }
    @Nonnull
    public String getTel() {
        return tel;
    }
}
