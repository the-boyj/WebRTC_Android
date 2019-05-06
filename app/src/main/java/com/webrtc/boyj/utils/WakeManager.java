package com.webrtc.boyj.utils;

import android.app.Activity;
import android.app.KeyguardManager;

import androidx.annotation.NonNull;

import static android.content.Context.KEYGUARD_SERVICE;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;
import static android.os.Build.VERSION_CODES.O_MR1;
import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
import static android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;

public class WakeManager {

    public static void turnOnScreen(@NonNull final Activity activity) {
        setShowWhenLocked(activity);
        setTurnScreenOn(activity);
        setKeepScreenOn(activity);
        requestDismissKeyguard(activity);
    }

    private static void setShowWhenLocked(@NonNull final Activity activity) {
        if (SDK_INT >= O_MR1) {
            activity.setShowWhenLocked(true);
        } else {
            activity.getWindow().addFlags(FLAG_SHOW_WHEN_LOCKED);
        }
    }

    private static void setTurnScreenOn(@NonNull final Activity activity) {
        if (SDK_INT >= O_MR1) {
            activity.setTurnScreenOn(true);
        } else {
            activity.getWindow().addFlags(FLAG_TURN_SCREEN_ON);
        }
    }

    private static void setKeepScreenOn(@NonNull final Activity activity) {
        activity.getWindow().addFlags(FLAG_KEEP_SCREEN_ON);
    }

    private static void requestDismissKeyguard(@NonNull final Activity activity) {
        if (SDK_INT >= O) {
            final KeyguardManager manager =
                    (KeyguardManager) activity.getSystemService(KEYGUARD_SERVICE);
            manager.requestDismissKeyguard(activity, null);
        } else {
            activity.getWindow().addFlags(FLAG_DISMISS_KEYGUARD);
        }
    }
}