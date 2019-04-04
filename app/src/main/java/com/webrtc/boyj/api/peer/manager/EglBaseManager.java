package com.webrtc.boyj.api.peer.manager;

import android.support.annotation.NonNull;

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
