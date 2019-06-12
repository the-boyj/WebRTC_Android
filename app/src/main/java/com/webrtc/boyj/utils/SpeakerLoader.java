package com.webrtc.boyj.utils;

import android.content.Context;
import android.media.AudioManager;

import androidx.annotation.NonNull;

import static android.content.Context.AUDIO_SERVICE;

public class SpeakerLoader {
    private final Context context;

    public SpeakerLoader(Context context) {
        this.context = context;
    }

    public void turnOn() {
        final AudioManager manager = getAudioManager();
        if (isSpeakerphoneOff(manager)) {
            manager.setSpeakerphoneOn(true);
        }
    }

    public void turnOff() {
        final AudioManager manager = getAudioManager();
        if (isSpeakerphoneOn(manager)) {
            manager.setSpeakerphoneOn(false);
        }
    }

    private AudioManager getAudioManager() {
        return (AudioManager) context.getSystemService(AUDIO_SERVICE);
    }

    private boolean isSpeakerphoneOn(@NonNull final AudioManager manager) {
        return manager.isSpeakerphoneOn();
    }

    private boolean isSpeakerphoneOff(@NonNull final AudioManager manager) {
        return !isSpeakerphoneOn(manager);
    }
}
