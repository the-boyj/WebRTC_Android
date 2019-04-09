package com.webrtc.boyj.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import java.util.concurrent.ThreadLocalRandom;

import static android.content.Context.TELEPHONY_SERVICE;

public class TelManager {
    private static final String TEL = "TEL";

    /**
     * 전화번호가 있는 경우 휴대폰 번호를, 없는 경우는 랜덤으로 생성한 번호를 반환한다.
     * 권한이 있어야만 호출되므로 경고에 안전하다.
     */
    @NonNull
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getTelNumber(@NonNull final Context context) {

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String tel = pref.getString(TEL, null);

        if (tel != null) {
            return tel;
        }

        final TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        final String number = tm.getLine1Number();

        tel = number == null ?
                generateTelNumber() :
                number.replace("+82", "0");

        pref.edit()
                .putString(TEL, tel)
                .apply();

        return tel;
    }

    private static String generateTelNumber() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        return String.format("010%s", random.nextInt(10000000, 100000000));
    }
}
