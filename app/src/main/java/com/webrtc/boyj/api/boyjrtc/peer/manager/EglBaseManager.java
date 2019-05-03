package com.webrtc.boyj.api.boyjrtc.peer.manager;

import androidx.annotation.NonNull;

import org.webrtc.EglBase;

public final class EglBaseManager {
    @NonNull
    private static EglBase eglBase = EglBase.create();

    private EglBaseManager() {

    }

    @NonNull
    public static EglBase getEglBase() {
        return eglBase;
    }
}
