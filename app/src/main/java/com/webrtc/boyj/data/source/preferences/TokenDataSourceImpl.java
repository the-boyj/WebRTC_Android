package com.webrtc.boyj.data.source.preferences;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class TokenDataSourceImpl implements TokenDataSource {
    private static final String KEY_DEVICE_TOKEN = "KEY_DEVICE_TOKEN";
    private static final String KEY_IS_NEW_TOKEN = "KEY_IS_NEW_TOKEN";

    @NonNull
    private final SharedPreferences pref;
    private static volatile TokenDataSource INSTANCE;

    private TokenDataSourceImpl(@NonNull final SharedPreferences pref) {
        this.pref = pref;
    }

    public static TokenDataSource getInstance(@NonNull final SharedPreferences pref) {
        if (INSTANCE == null) {
            synchronized (TokenDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TokenDataSourceImpl(pref);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public boolean isNewToken() {
        return pref.getBoolean(KEY_IS_NEW_TOKEN, false);
    }


    @Override
    public void registerToken(@NonNull String token) {
        pref.edit()
                .putString(KEY_DEVICE_TOKEN, token)
                .putBoolean(KEY_IS_NEW_TOKEN, true)
                .apply();
    }

    @Override
    public String getToken() {
        return pref.getString(KEY_DEVICE_TOKEN, null);
    }

    @Override
    public void unsetNewToken() {
        pref.edit()
                .putBoolean(KEY_IS_NEW_TOKEN, false)
                .apply();
    }
}
