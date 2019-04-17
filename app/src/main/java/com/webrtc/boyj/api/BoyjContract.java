package com.webrtc.boyj.api;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.api.signalling.payload.RejectPayload;

@SuppressWarnings("SpellCheckingInspection")
public interface BoyjContract {
    /**
     * 처음으로 통화를 요청할 경우 room 을 생성한다.
     *
     * @param createRoomPayload room, callerId 가 담긴 페이로드
     */
    void createRoom(@NonNull CreateRoomPayload createRoomPayload);

    /**
     * Caller 가 상대방에게 통화를 요청한다.
     *
     * @param dialPayload calleeId가 담긴 페이로드
     */
    void dial(@NonNull DialPayload dialPayload);

    /**
     * Callee 가 푸시 알람을 수신 후 서버로 응답한다.
     *
     * @param awakenPayload room, calleeId가 담긴 페이로드
     */
    void awaken(@NonNull AwakenPayload awakenPayload);

    /**
     * Callee 가 전화를 승인한 후 커넥션 생성 및 Accept 를 서버로 전송한다.
     *
     * @param callerId 전화가 걸려온 상대 아이디
     */
    void accept(@NonNull String callerId);

    void reject(@NonNull RejectPayload rejectPayload);

    void bye();
}
