package com.webrtc.boyj.api.signalling;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.signalling.payload.EndOfCallPayload;
import com.webrtc.boyj.api.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.signalling.payload.RejectPayload;
import com.webrtc.boyj.api.signalling.payload.SdpPayload;

public interface SignalingCallback {
    void onOfferSdpPayloadFromSig(@NonNull final SdpPayload sdpPayload);

    void onAnswerSdpPayloadFromSig(@NonNull final SdpPayload sdpPayload);

    void onIceCandidatePayloadFromSig(@NonNull final IceCandidatePayload iceCandidatePayload);

    void onRejectPayloadFromSig(@NonNull final RejectPayload rejectPayload);

    void onEndOfCallPayloadFromSig(@NonNull final EndOfCallPayload endOfCallPayload);
}
