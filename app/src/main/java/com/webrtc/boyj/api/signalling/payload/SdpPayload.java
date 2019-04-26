package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import org.webrtc.SessionDescription;

public class SdpPayload extends Payload {
    private SessionDescription sdp;
    private String sender;
    private String receiver;

    public SdpPayload() {

    }

    public SdpPayload(@NonNull final SessionDescription sdp) {
        this.sdp = sdp;
    }

    public SessionDescription getSdp() {
        return sdp;
    }

    public void setSdp(SessionDescription sdp) {
        this.sdp = sdp;
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

    public SessionDescription.Type getType() {
        return sdp.type;
    }
}
