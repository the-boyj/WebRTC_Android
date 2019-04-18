package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.webrtc.boyj.utils.JSONUtil;

public class EndOfCallPayload {
    private String sender;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONUtil.toJson(getClass(), this);
    }
}
