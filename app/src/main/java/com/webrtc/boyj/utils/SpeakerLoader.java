package com.webrtc.boyj.utils;

import android.content.Context;
import android.media.AudioManager;

import androidx.annotation.NonNull;

import static android.content.Context.AUDIO_SERVICE;

public class SpeakerLoader {

    public static void turnOn(@NonNull final Context context) {
        final AudioManager manager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        if (isSpeakerphoneOff(manager)) {
            manager.setSpeakerphoneOn(true);
        }
    }

    public static void turnOff(@NonNull final Context context) {
        final AudioManager manager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        if (isSpeakerphoneOn(manager)) {
            manager.setSpeakerphoneOn(false);
        }
    }

    private static boolean isSpeakerphoneOn(@NonNull final AudioManager manager) {
        return manager.isSpeakerphoneOn();
    }

    private static boolean isSpeakerphoneOff(@NonNull final AudioManager manager) {
        return !isSpeakerphoneOn(manager);
    }
}
