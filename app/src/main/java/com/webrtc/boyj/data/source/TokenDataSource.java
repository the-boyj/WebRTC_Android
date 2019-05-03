package com.webrtc.boyj.data.source;

import androidx.annotation.NonNull;

public interface TokenDataSource {

    boolean isNewToken();

    void registerToken(@NonNull final String token);

    String getToken();

    void unsetNewToken();
}
