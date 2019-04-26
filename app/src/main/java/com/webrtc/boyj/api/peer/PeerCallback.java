package com.webrtc.boyj.api.peer;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.signalling.payload.SdpPayload;
import com.webrtc.boyj.data.model.BoyjMediaStream;

public interface PeerCallback {
    void onOfferSdpPayloadFromPeer(@NonNull final SdpPayload sdpPayload);

    void onAnswerSdpPayloadFromPeer(@NonNull final SdpPayload sdpPayload);

    void onIceCandidatePayloadFromPeer(@NonNull final IceCandidatePayload iceCandidatePayload);

    void onRemoteStreamFromPeer(@NonNull final BoyjMediaStream mediaStream);
}
