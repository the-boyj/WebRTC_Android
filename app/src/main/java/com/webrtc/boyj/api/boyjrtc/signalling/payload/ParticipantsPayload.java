package com.webrtc.boyj.api.boyjrtc.signalling.payload;

import java.util.List;

public class ParticipantsPayload extends Payload {
    private List<Participant> participants;
    private int length;

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
