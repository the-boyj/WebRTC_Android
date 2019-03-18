package com.webrtc.boyj.presentation.ringing;

import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.api.signalling.SignalingClientFactory;

public class RingingViewModel {
    public void acceptAction(){
        SignalingClient signalingClient = SignalingClientFactory.getSignalingClient();
        signalingClient.emitAccept();
    }
    public void rejectAction(){
        SignalingClient signalingClient = SignalingClientFactory.getSignalingClient();
        signalingClient.emitReject();
    }
}
