package com.webrtc.boyj.data.source.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webrtc.boyj.utils.JSONUtil;

public class UserItem {
    @SerializedName("userId")
    @Expose
    private final String userId;

    @SerializedName("userName")
    @Expose
    private final String userName;

    public UserItem(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return JSONUtil.toJson(this);
    }
}
