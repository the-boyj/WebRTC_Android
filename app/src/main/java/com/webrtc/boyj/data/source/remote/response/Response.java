package com.webrtc.boyj.data.source.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webrtc.boyj.utils.JSONUtil;

public class Response<T> {
    @SerializedName("code")
    @Expose
    private final int code;

    @SerializedName("data")
    @Expose
    private final T item;

    public Response(int code, T item) {
        this.code = code;
        this.item = item;
    }

    public int getCode() {
        return code;
    }

    public T getItem() {
        return item;
    }

    @Override
    public String toString() {
        return JSONUtil.toJson(this);
    }
}
