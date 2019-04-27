package com.webrtc.boyj.api.boyjrtc.signalling.payload;

import android.support.annotation.NonNull;

import com.webrtc.boyj.utils.JSONUtil;

public abstract class Payload {
    @NonNull
    @Override
    public String toString() {
        return JSONUtil.toJson(getClass(), this);
    }
}
