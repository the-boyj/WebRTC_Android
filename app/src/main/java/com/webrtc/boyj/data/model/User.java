package com.webrtc.boyj.data.model;

import android.support.annotation.NonNull;

public class User {
    @NonNull
    private final String name;
    @NonNull
    private final String deviceToken;

    public User(@NonNull String name,
                @NonNull String deviceToken) {
        this.name = name;
        this.deviceToken = deviceToken;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getDeviceToken() {
        return deviceToken;
    }
}