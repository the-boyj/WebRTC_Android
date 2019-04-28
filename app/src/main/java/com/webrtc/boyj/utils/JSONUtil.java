package com.webrtc.boyj.utils;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.Payload;

import org.json.JSONObject;

public class JSONUtil {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @NonNull
    public static JsonObject toJsonObject(@NonNull final Object object) {
        return new JsonParser().parse(toJson(object)).getAsJsonObject();
    }

    @NonNull
    public static String toJson(@NonNull final Object object) {
        return gson.toJson(object);
    }

    @NonNull
    public static <T extends Payload> T fromJson(@NonNull final Object object,
                                                 @NonNull final Class<T> type) {
        final JSONObject jsonObject = (JSONObject) object;
        return gson.fromJson(jsonObject.toString(), type);
    }
}
