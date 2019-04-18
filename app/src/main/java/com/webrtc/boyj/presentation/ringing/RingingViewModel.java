package com.webrtc.boyj.presentation.ringing;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.RejectPayload;
import com.webrtc.boyj.presentation.BaseViewModel;

public class RingingViewModel extends BaseViewModel {
    @NonNull
    private final ObservableField<String> callerId = new ObservableField<>();
    private BoyjRTC boyjRTC;

    public RingingViewModel() {
        this.boyjRTC = new BoyjRTC();
    }

    public void awaken(@NonNull final String room, // FCM으로 전달받은 room
                       @NonNull final String callerId, // 통화가 걸려온 상대 ID
                       @NonNull final String calleeId) { // 통화 받은 본인 ID
        this.callerId.set(callerId);
        final AwakenPayload payload = new AwakenPayload.Builder(room, callerId, calleeId).build();
        boyjRTC.awaken(payload);
    }

    public void reject(@NonNull final String callerId) {
        final RejectPayload payload = new RejectPayload();
        payload.setReceiver(callerId);
        boyjRTC.reject(payload);
    }

    @NonNull
    public ObservableField<String> getCallerId() {
        return callerId;
    }
}
