package com.webrtc.boyj.api.boyjrtc.signalling.payload;

import androidx.annotation.NonNull;

import java.util.UUID;

public class CreateRoomPayload extends Payload {
    @NonNull
    private final String room; // UUID로 생성되는 방 정보
    @NonNull
    private final String callerId; // 통화 송신자의 식별 번호

    public CreateRoomPayload(@NonNull final String callerId) {
        this.room = createRoom();
        this.callerId = callerId;
    }

    private static String createRoom() {
        return UUID.randomUUID().toString();
    }
}
