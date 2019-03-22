package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

public abstract class Payload {

    @NonNull
    public String toJson() {
        final Gson gson = new Gson();

        return gson.toJson(this);
    }
}
