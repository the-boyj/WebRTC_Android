package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import org.webrtc.IceCandidate;

public class IceCandidatePayload extends Payload {

    @NonNull
    private IceCandidate iceCandidate;

    public void setIceCandidate(@NonNull final IceCandidate iceCandidate) {
        this.iceCandidate = iceCandidate;
    }

    public static class Builder {
        @NonNull
        private final IceCandidate iceCandidate;

        public Builder(@NonNull final IceCandidate iceCandidate) {
            this.iceCandidate = iceCandidate;
        }

        @NonNull
        public IceCandidatePayload build() {
            final IceCandidatePayload iceCandidatePayload = new IceCandidatePayload();
            iceCandidatePayload.setIceCandidate(this.iceCandidate);
            return iceCandidatePayload;
        }
    }


}



