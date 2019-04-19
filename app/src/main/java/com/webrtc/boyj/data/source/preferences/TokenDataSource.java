package com.webrtc.boyj.data.source.preferences;

import android.support.annotation.NonNull;

public interface TokenDataSource {
    /**
     * 현재 토큰이 최신 상태인지에 대한 여부를 반환한다.
     */
    boolean isNewToken();

    /**
     * Preferences 에 인자로 받은 토큰을 등록한다.
     */
    void registerToken(@NonNull final String token);

    /**
     * Preferences 에 들어있는 토큰 값 반환
     */
    String getToken();

    /**
     * Token 을 서버에 등록한 이후 newToken 상태 해제
     */
    void unsetNewToken();

}
