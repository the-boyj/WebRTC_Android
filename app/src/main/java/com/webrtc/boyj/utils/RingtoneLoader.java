package com.webrtc.boyj.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.NonNull;

public class RingtoneLoader {
    private static Ringtone ringtone;
    private static Vibrator vibrator;

    public static void ring(@NonNull final Context context) {
        ringtone = getDefaultRingtone(context);
        vibrator = getVibrator(context);

        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            ringtone.play();
        } else if (am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
            vibrate();
        }
    }

    private static Ringtone getDefaultRingtone(@NonNull final Context context) {
        if (ringtone == null) {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), uri);
        }
        return ringtone;
    }

    private static Vibrator getVibrator(@NonNull final Context context) {
        if (vibrator == null) {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
        return vibrator;
    }

    private static void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(new long[]{500, 1000}, 1));
        } else {
            vibrator.vibrate(new long[]{500, 1000}, 1);
        }
    }

    public static void cancelAndRelease() {
        if (ringtone != null) {
            ringtone.stop();
            ringtone = null;
        }
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
    }
}
