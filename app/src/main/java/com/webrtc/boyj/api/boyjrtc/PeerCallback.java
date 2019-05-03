package com.webrtc.boyj.api.boyjrtc;

import androidx.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.SdpPayload;

public interface PeerCallback {
    void onOfferSdpPayloadFromPeer(@NonNull final SdpPayload sdpPayload);

    void onAnswerSdpPayloadFromPeer(@NonNull final SdpPayload sdpPayload);

    void onIceCandidatePayloadFromPeer(@NonNull final IceCandidatePayload iceCandidatePayload);

    void onRemoteStreamFromPeer(@NonNull final BoyjMediaStream mediaStream);

    void onCallFinish();
}
