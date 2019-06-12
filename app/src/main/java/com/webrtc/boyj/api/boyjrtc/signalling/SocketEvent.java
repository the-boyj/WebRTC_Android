package com.webrtc.boyj.api.boyjrtc.signalling;

import androidx.annotation.NonNull;

public enum SocketEvent {
    /* Caller */
    CONNECTION_ACK("CONNECTION_ACK"),
    CREATE_ROOM("CREATE_ROOM"),
    DIAL("DIAL"),
    NOTIFY_REJECT("NOTIFY_REJECT"),
    RELAY_OFFER("RELAY_OFFER"),
    ANSWER("ANSWER"),

    /* Callee */
    AWAKEN("AWAKEN"),
    REJECT("REJECT"),
    ACCEPT("ACCEPT"),
    PARTICIPANTS("PARTICIPANTS"),
    OFFER("OFFER"),
    RELAY_ANSWER("RELAY_ANSWER"),

    /* Caller & Callee */
    SEND_ICE_CANDIDATE("SEND_ICE_CANDIDATE"),
    RELAY_ICE_CANDIDATE("RELAY_ICE_CANDIDATE"),
    END_OF_CALL("END_OF_CALL"),
    NOTIFY_END_OF_CALL("NOTIFY_END_OF_CALL"),
    PEER_TO_SERVER_ERROR("PEER_TO_SERVER_ERROR"),
    SERVER_TO_PEER_ERROR("SERVER_TO_PEER_ERROR");

    private final String event;

    SocketEvent(String event) {
        this.event = event;
    }

    @NonNull
    @Override
    public String toString() {
        return event;
    }
}
