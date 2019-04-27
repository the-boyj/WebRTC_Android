package com.webrtc.boyj.api.boyjrtc.peer.observer;

import android.support.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.PeerCallback;

import org.webrtc.SessionDescription;

public class NoOpSdpObserver extends BoyjSdpObserver {

    public NoOpSdpObserver(@NonNull String id, @NonNull PeerCallback callback) {
        super(id, callback);
    }

    @Override
    protected void onSessionDescription(@NonNull SessionDescription sessionDescription) {
        // doNothing
    }
}
