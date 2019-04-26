package com.webrtc.boyj.api.peer.observer;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.peer.PeerCallback;
import com.webrtc.boyj.api.signalling.payload.SdpPayload;

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
        return new SdpPayload(sdp);
    }

}
