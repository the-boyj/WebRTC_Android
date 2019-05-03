package com.webrtc.boyj.api.boyjrtc;

import androidx.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.signalling.payload.EndOfCallPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.IceCandidatePayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.ParticipantsPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.RejectPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.SdpPayload;

public interface SignalingCallback {
    void onParticipantsPayloadFromSig(@NonNull final ParticipantsPayload participantsPayload);

    void onOfferSdpPayloadFromSig(@NonNull final SdpPayload sdpPayload);

    void onAnswerSdpPayloadFromSig(@NonNull final SdpPayload sdpPayload);

    void onIceCandidatePayloadFromSig(@NonNull final IceCandidatePayload iceCandidatePayload);

    void onRejectPayloadFromSig(@NonNull final RejectPayload rejectPayload);

    void onEndOfCallPayloadFromSig(@NonNull final EndOfCallPayload endOfCallPayload);
}
