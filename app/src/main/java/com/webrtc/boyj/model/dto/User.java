package com.webrtc.boyj.model.dto;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String tel;
    private String deviceToken;

    public User() {
        this.name = "unknown";
        this.tel = "000-0000-0000";
        this.deviceToken = "";
    }

    public User(String name, String tel, String deviceToken) {
        this.name = name;
        this.tel = tel;
        this.deviceToken = deviceToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
