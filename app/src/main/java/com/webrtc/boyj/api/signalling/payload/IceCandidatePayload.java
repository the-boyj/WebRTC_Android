package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.webrtc.boyj.utils.JSONUtil;

import org.webrtc.IceCandidate;

public class IceCandidatePayload {
    private IceCandidate iceCandidate;
    private String sender;
    private String receiver;

    public IceCandidatePayload() {

    }

    public IceCandidatePayload(@NonNull final IceCandidate iceCandidate) {
        this.iceCandidate = iceCandidate;
    }

    public IceCandidate getIceCandidate() {
        return iceCandidate;
    }

    public void setIceCandidate(IceCandidate iceCandidate) {
        this.iceCandidate = iceCandidate;
    }

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
