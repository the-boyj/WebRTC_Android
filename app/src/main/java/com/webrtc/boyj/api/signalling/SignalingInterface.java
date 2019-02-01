package com.webrtc.boyj.api.signalling;

public interface SignalingInterface {
    String EVENT_DIAL = "dial";
    String EVENT_CREATED = "created";
    String EVENT_AWAKEN = "awaken";
    String EVENT_KNOCK = "knock";
    String EVENT_ACCEPT = "accept";
    String EVENT_REJECT = "reject";

    String EVENT_READY = "ready";

    String EVENT_SEND_SDP = "ssdp";
    String EVENT_RECEIVE_SDP = "rsdp";

    String EVENT_SEND_ICE = "sice";
    String EVENT_RECEIVE_ICE = "rice";

    String EVENT_BYE = "bye";
    String EVENT_LOG = "log";

    String EVENT_SERVER_ERROR = "serverError";
    String EVENT_PEER_ERROR = "peer_error";
}
