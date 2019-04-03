package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public abstract class Payload implements Serializable {

    @NonNull
    public JSONObject toJsonObject() {
        final Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(this));
        } catch (JSONException e) {
            throw new IllegalStateException("object is not valid");
        }
        return jsonObject;
    }
}
