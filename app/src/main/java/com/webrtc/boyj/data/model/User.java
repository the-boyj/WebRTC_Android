package com.webrtc.boyj.data.model;

import android.support.annotation.NonNull;

public class User {
    @NonNull
    private final String name;
    @NonNull
    private final String tel;
    @NonNull
    private final String deviceToken;

    public User(@NonNull String name,
                @NonNull String tel,
                @NonNull String deviceToken) {
        this.name = name;
        this.tel = tel;
        this.deviceToken = deviceToken;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getTel() {
        return tel;
    }

    @NonNull
    public String getDeviceToken() {
        return deviceToken;
    }
}