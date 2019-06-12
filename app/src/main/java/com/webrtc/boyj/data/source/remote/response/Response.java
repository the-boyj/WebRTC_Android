package com.webrtc.boyj.data.source.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webrtc.boyj.utils.JSONUtil;

public class Response<T> {
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
    private final T item;

    public Response(int code, String status, String message, T item) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.item = item;
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

    public T getItem() {
        return item;
    }

    @Override
    public String toString() {
        return JSONUtil.toJson(this);
    }
}
