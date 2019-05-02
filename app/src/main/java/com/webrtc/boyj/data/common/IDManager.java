package com.webrtc.boyj.data.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class IDManager {
    private static final String KEY_USER_ID = "KEY_USER_ID";

    @Nullable
    public static String getSavedUserId(@NonNull final Context context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(KEY_USER_ID, null);
    }

    public static void saveUserId(@NonNull final Context context,
                                  @NonNull final String id) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString(KEY_USER_ID, id).apply();
    }
}
