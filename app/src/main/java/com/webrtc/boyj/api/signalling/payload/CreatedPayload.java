package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.webrtc.boyj.utils.JSONUtil;

public class CreatedPayload {
    private String calleeId;

    public void setCalleeId(String calleeId) {
        this.calleeId = calleeId;
    }

    @NonNull
    public String getCalleeId() {
        return calleeId;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONUtil.toJson(getClass(), this);
    }
}
