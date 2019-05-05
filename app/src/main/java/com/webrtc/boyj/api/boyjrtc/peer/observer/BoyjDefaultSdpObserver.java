package com.webrtc.boyj.api.boyjrtc.peer.observer;

import androidx.annotation.NonNull;

import com.webrtc.boyj.utils.Logger;

import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

public class BoyjDefaultSdpObserver implements SdpObserver {
    @NonNull
    protected final String id;

    public BoyjDefaultSdpObserver(@NonNull final String id) {
        this.id = id;
    }

    @Override
    public void onSetSuccess() {
        Logger.d(id + "onSetSuccess() called");
    }

    @Override
    public void onSetFailure(String s) {
        Logger.d(id + "onSetFailure() called withLayout: s = [" + s + "]");
    }

    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {
        Logger.d(id + "onCreateSuccess() called withLayout s = [" + sessionDescription + "]");
    }

    @Override
    public void onCreateFailure(String s) {
        Logger.d(id + "onCreateFailure() called withLayout: s = [" + s + "]");
    }
}
