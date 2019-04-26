package com.webrtc.boyj.api.signalling.payload;

public class EndOfCallPayload extends Payload {
    private String sender;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
