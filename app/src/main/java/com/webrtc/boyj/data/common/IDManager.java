package com.webrtc.boyj.data.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

public class IDManager {
    private static final String KEY_USER_ID = "KEY_USER_ID";

    @NonNull
    public static String getSavedUserId(@NonNull final Context context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        final String userId = pref.getString(KEY_USER_ID, null);
        if (userId == null) {
            throw new IllegalStateException("User ID is not allocated");
        }
        return userId;
    }

    public static void saveUserId(@NonNull final Context context,
                                  @NonNull final String id) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString(KEY_USER_ID, id).apply();
    }
}
