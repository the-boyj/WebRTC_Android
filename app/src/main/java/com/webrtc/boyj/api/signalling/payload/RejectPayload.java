package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.webrtc.boyj.utils.JSONUtil;

public class RejectPayload {
    private String sender;
    private String receiver;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONUtil.toJson(getClass(), this);
    }
}
