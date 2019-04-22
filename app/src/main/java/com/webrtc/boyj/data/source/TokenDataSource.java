package com.webrtc.boyj.data.source;

import android.support.annotation.NonNull;

public interface TokenDataSource {

    boolean isNewToken();

    void registerToken(@NonNull final String token);

    String getToken();

    void unsetNewToken();
}
