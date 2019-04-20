package com.webrtc.boyj.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private String tel;
    private String name;
    private String deviceToken;

    public User() {
    }

    public User(String tel,
                String name,
                String deviceToken) {
        this.tel = tel;
        this.name = name;
        this.deviceToken = deviceToken;
    }

    public static User createFromId(@NonNull final String id) {
        return new User(id, null, null);
    }

    public String getTel() {
        return tel;
    }

    public String getName() {
        return name;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o instanceof User) {
            User user = (User) o;
            return Objects.equals(tel, user.tel) &&
                    Objects.equals(name, user.name) &&
                    Objects.equals(deviceToken, user.deviceToken);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tel, name, deviceToken);
    }
}