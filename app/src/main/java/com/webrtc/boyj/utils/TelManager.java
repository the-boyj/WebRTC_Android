package com.webrtc.boyj.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import static android.content.Context.TELEPHONY_SERVICE;

public class TelManager {

    @Nullable
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getTelNumber(@NonNull final Context context) {

        final TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        final String number = tm.getLine1Number();

        if (TextUtils.isEmpty(number)) {
            return null;
        } else {
            return number.replace("+82", "0");
        }
    }
}
