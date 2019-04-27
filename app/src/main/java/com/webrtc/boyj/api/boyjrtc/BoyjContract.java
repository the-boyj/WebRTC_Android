package com.webrtc.boyj.api.boyjrtc;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.DialPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.RejectPayload;

public interface BoyjContract {
    /**
     * 처음으로 통화를 요청할 경우 room 을 생성한다.
     */
    void createRoom(@NonNull final CreateRoomPayload payload);

    /**
     * Caller 가 상대방에게 통화를 요청한다.
     */
    void dial(@NonNull final DialPayload payload);

    /**
     * Callee 가 푸시 알람을 수신 후 서버로 응답한다.
     */
    void awaken(@NonNull final AwakenPayload payload);

    /**
     * Callee 가 전화를 승인한 후 커넥션 생성 및 Accept 를 서버로 전송한다.
     */
    void accept();

    /**
     * 통화를 수신받은 사용자가 요청을 거부할 경우 응답 결과를 시그너링 서버로 전송한다.
     */
    void reject(@NonNull final RejectPayload payload);

    /**
     * 통화중인 사용자가 종료 버튼을 통해 통화를 종료한 경우 호출한다.
     */
    void endOfCall();
}
