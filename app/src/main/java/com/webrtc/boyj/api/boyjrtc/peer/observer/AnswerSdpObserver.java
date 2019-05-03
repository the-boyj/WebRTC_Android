package com.webrtc.boyj.api.boyjrtc.peer.observer;

import androidx.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.PeerCallback;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.SdpPayload;

import org.webrtc.SessionDescription;

public class AnswerSdpObserver extends BoyjSdpObserver {

    public AnswerSdpObserver(@NonNull String id,
                             @NonNull PeerCallback callback) {
        super(id, callback);
    }

    @Override
    protected void onSessionDescription(@NonNull SessionDescription sessionDescription) {
        final SdpPayload payload = createSdpPayload(sessionDescription);
        callback.onAnswerSdpPayloadFromPeer(payload);
    }

    private SdpPayload createSdpPayload(@NonNull SessionDescription sdp) {
        final SdpPayload payload = new SdpPayload(sdp);
        payload.setReceiver(getId());
        return payload;
    }
}
