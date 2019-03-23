package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.Serializable;

public abstract class Payload implements Serializable {

    @NonNull
    public String toJson() {
        final Gson gson = new Gson();

        return gson.toJson(this);
    }
}
