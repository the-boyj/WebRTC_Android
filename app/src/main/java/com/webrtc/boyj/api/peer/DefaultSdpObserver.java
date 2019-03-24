package com.webrtc.boyj.api.peer;

import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

public abstract class DefaultSdpObserver implements SdpObserver {

    @Override
    public abstract void onCreateSuccess(SessionDescription sessionDescription);

    @Override
    public void onSetSuccess() {
    }

    @Override
    public void onCreateFailure(String s) {
    }

    @Override
    public void onSetFailure(String s) {
    }
}
