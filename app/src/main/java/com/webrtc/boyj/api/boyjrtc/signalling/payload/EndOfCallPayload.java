package com.webrtc.boyj.api.boyjrtc.signalling.payload;

public class EndOfCallPayload extends Payload {
    private String sender;
    private boolean timeout;

    public boolean getTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
