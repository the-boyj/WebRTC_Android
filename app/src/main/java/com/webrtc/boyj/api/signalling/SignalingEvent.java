package com.webrtc.boyj.api.signalling;

import android.support.annotation.NonNull;

public enum SignalingEvent {
    CREATE_ROOM("CREATE_ROOM"),
    DIAL("DIAL"),
    ANSWER("ANSWER"),
    NOTIFY_REJECT("NOTIFY_REJECT"),
    RELAY_OFFER("RELAY_OFFER"),

    AWAKEN("AWAKEN"),
    ACCEPT("ACCEPT"),
    REJECT("REJECT"),
    RELAY_ANSWER("RELAY_ANSWER"),

    SEND_ICE_CANDIDATE("SEND_ICE_CANDIDATE"),
    RELAY_ICE_CANDIDATE("RELAY_ICE_CANDIDATE"),
    END_OF_CALL("END_OF_CALL"),
    NOTIFY_END_OF_CALL("NOTIFY_END_OF_CALL"),
    PEER_TO_SERVER_ERROR("PEER_TO_SERVER_ERROR"),
    SERVER_TO_PEER_ERROR("SERVER_TO_PEER_ERROR");

    private final String event;

    SignalingEvent(String event) {
        this.event = event;
    }

    @NonNull
    @Override
    public String toString() {
        return event;
    }
}
