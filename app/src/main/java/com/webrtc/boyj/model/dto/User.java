package com.webrtc.boyj.model.dto;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String tel;
    private String deviceToken;

    // TODO : Firebase 완성되면 삭제
    public User(String name, String tel,String deviceToken) {
        this.name = name;
        this.tel = tel;
        this.deviceToken=deviceToken;
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
