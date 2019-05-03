package com.webrtc.boyj.api.boyjrtc.peer.observer;

import androidx.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.PeerCallback;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.SdpPayload;

import org.webrtc.SessionDescription;

public class OfferSdpObserver extends BoyjSdpObserver {

    public OfferSdpObserver(@NonNull String id,
                            @NonNull PeerCallback callback) {
        super(id, callback);
    }

    protected void onSessionDescription(@NonNull final SessionDescription sessionDescription) {
        final SdpPayload payload = createSdpPayload(sessionDescription);
        callback.onOfferSdpPayloadFromPeer(payload);
    }

    private SdpPayload createSdpPayload(@NonNull final SessionDescription sdp) {
        final SdpPayload payload = new SdpPayload(sdp);
        payload.setReceiver(getId());
        return payload;
    }

}
