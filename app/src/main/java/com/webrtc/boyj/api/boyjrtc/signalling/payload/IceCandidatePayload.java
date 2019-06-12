package com.webrtc.boyj.api.boyjrtc.signalling.payload;

import androidx.annotation.NonNull;

import org.webrtc.IceCandidate;

public class IceCandidatePayload extends Payload {
    private IceCandidate iceCandidate;
    private String sender;
    private String receiver;

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
}
