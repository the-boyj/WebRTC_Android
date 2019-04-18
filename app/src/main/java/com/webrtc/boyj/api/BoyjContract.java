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

    /**
     * 통화를 수신받은 사용자가 요청을 거부할 경우 응답 결과를 시그너링 서버로 전송한다.
     *
     * @param rejectPayload : sender가 담긴 페이로드
     */
    void reject(@NonNull RejectPayload rejectPayload);

    /**
     * 통화중인 사용자가 종료 버튼을 통해 통화를 종료한 경우 호출한다.
     */
    void endOfCall();
}
