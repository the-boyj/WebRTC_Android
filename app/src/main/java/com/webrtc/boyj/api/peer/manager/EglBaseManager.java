package com.webrtc.boyj.api.peer.manager;

import android.support.annotation.NonNull;

import org.webrtc.EglBase;

public class EglBaseManager {
    @NonNull
    private static EglBase eglBase;

    static {
        eglBase = EglBase.create();
    }

    @NonNull
    public static EglBase.Context getEglBaseContext() {
        return eglBase.getEglBaseContext();
    }

    public static void release() {
        eglBase.release();
    }
}
