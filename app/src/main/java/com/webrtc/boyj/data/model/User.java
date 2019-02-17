package com.webrtc.boyj.data.model;

public class User {
    private String name;
    private String tel;
    private String deviceToken;

    public User() {
    }

    public User(String name,
                String tel,
                String deviceToken) {
        this.name = name;
        this.tel = tel;
        this.deviceToken = deviceToken;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getDeviceToken() {
        return deviceToken;
    }
}