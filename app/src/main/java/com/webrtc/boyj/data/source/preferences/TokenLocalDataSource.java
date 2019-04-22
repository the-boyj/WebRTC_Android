package com.webrtc.boyj.data.source.preferences;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.webrtc.boyj.data.source.TokenDataSource;

public class TokenLocalDataSource implements TokenDataSource {
    private static final String KEY_DEVICE_TOKEN = "KEY_DEVICE_TOKEN";
    private static final String KEY_IS_NEW_TOKEN = "KEY_IS_NEW_TOKEN";

    @NonNull
    private final SharedPreferences pref;
    private static volatile TokenDataSource INSTANCE;

    private TokenLocalDataSource(@NonNull final SharedPreferences pref) {
        this.pref = pref;
    }

    public static TokenDataSource getInstance(@NonNull final SharedPreferences pref) {
        if (INSTANCE == null) {
            synchronized (TokenLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TokenLocalDataSource(pref);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 현재 토큰이 최신 상태인지에 대한 여부를 반환한다.
     */
    @Override
    public boolean isNewToken() {
        return pref.getBoolean(KEY_IS_NEW_TOKEN, false);
    }

    /**
     * Preferences 에 인자로 받은 토큰을 등록한다.
     */
    @Override
    public void registerToken(@NonNull String token) {
        pref.edit()
                .putString(KEY_DEVICE_TOKEN, token)
                .putBoolean(KEY_IS_NEW_TOKEN, true)
                .apply();
    }

    /**
     * Preferences 에 들어있는 토큰 값 반환
     */
    @Override
    public String getToken() {
        return pref.getString(KEY_DEVICE_TOKEN, null);
    }

    /**
     * Token 을 서버에 등록한 이후 newToken 상태 해제
     */
    @Override
    public void unsetNewToken() {
        pref.edit()
                .putBoolean(KEY_IS_NEW_TOKEN, false)
                .apply();
    }
}
