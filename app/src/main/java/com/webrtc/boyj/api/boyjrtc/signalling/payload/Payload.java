package com.webrtc.boyj.api.boyjrtc.signalling.payload;

import androidx.annotation.NonNull;

import com.webrtc.boyj.utils.JSONUtil;

public abstract class Payload {
    @NonNull
    @Override
    public String toString() {
        return JSONUtil.toJson(this);
    }
}
