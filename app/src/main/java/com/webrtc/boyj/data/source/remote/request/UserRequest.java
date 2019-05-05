package com.webrtc.boyj.data.source.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRequest {
    @SerializedName("userId")
    @Expose
    private final String id;

    @SerializedName("name")
    @Expose
    private final String name;

    @SerializedName("deviceToken")
    @Expose
    private final String deviceToken;

    public UserRequest(String id, String name, String deviceToken) {
        this.id = id;
        this.name = name;
        this.deviceToken = deviceToken;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDeviceToken() {
        return deviceToken;
    }
}
