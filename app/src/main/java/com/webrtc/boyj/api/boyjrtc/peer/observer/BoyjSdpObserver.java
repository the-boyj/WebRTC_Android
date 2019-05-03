package com.webrtc.boyj.api.boyjrtc.peer.observer;

import androidx.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.PeerCallback;
import com.webrtc.boyj.utils.Logger;

import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

public abstract class BoyjSdpObserver implements SdpObserver {
    @NonNull
    private final String id;
    protected final PeerCallback callback;

    public BoyjSdpObserver(@NonNull final String id,
                           @NonNull final PeerCallback callback) {
        this.id = id;
        this.callback = callback;
    }

    @NonNull
    public String getId() {
        return id;
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
        onSessionDescription(sessionDescription);
    }

    protected abstract void onSessionDescription(@NonNull final SessionDescription sessionDescription);

    @Override
    public void onCreateFailure(String s) {
        Logger.d(id + "onCreateFailure() called withLayout: s = [" + s + "]");
    }
}
