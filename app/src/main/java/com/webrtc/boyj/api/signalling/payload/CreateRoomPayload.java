package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.webrtc.boyj.utils.JSONUtil;

import java.util.UUID;

public class CreateRoomPayload {
    @NonNull
    private final String room; // UUID로 생성되는 방 정보
    @NonNull
    private final String callerId; // 통화 송신자의 식별 번호

    private CreateRoomPayload(@NonNull final String room,
                              @NonNull final String callerId) {
        this.room = room;
        this.callerId = callerId;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONUtil.toJson(getClass(), this);
    }

    public static class Builder {
        @NonNull
        private final String room;
        @NonNull
        private final String callerId;

        public Builder(@NonNull final String callerId) {
            this.room = createRoom();
            this.callerId = callerId;
        }

        public CreateRoomPayload build() {
            return new CreateRoomPayload(room, callerId);
        }

        private static String createRoom() {
            return UUID.randomUUID().toString();
        }
    }
}
