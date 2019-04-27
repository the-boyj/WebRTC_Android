package com.webrtc.boyj.api.boyjrtc;

import android.support.annotation.NonNull;

import org.webrtc.MediaStream;

public class BoyjMediaStream {
    @NonNull
    private final String id;
    @NonNull
    private final MediaStream mediaStream;

    public BoyjMediaStream(@NonNull String id,
                           @NonNull MediaStream mediaStream) {
        this.id = id;
        this.mediaStream = mediaStream;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public MediaStream getMediaStream() {
        return mediaStream;
    }
}
