package com.webrtc.boyj.data.source.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webrtc.boyj.utils.JSONUtil;

import java.util.List;

public class ListResponse<T> {
    @SerializedName("code")
    @Expose
    private final int code;

    @SerializedName("status")
    @Expose
    private final String status;

    @SerializedName("message")
    @Expose
    private final String message;

    @SerializedName("data")
    @Expose
    private final List<T> items;

    public ListResponse(int code, String status, String message, List<T> items) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.items = items;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return JSONUtil.toJson(this);
    }
}
