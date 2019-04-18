package com.webrtc.boyj.api.peer;

import android.util.Log;

import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

class LogSdpObserver implements SdpObserver {
    private String tag = getClass().getSimpleName();

    LogSdpObserver(String logTag) {
        this.tag += logTag;
    }

    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {
        Log.d(tag, "onCreateSuccess() called with: sessionDescription = [" + sessionDescription + "]");
    }

    @Override
    public void onSetSuccess() {
        Log.d(tag, "onSetSuccess() called");
    }

    @Override
    public void onCreateFailure(String s) {
        Log.d(tag, "onCreateFailure() called with: s = [" + s + "]");
    }

    @Override
    public void onSetFailure(String s) {
        Log.d(tag, "onSetFailure() called with: s = [" + s + "]");
    }
}
