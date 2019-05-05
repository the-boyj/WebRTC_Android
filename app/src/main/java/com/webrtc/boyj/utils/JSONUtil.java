package com.webrtc.boyj.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.Payload;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Nullable
    public static JSONObject toJSONObject(@NonNull final Object object) {
        try {
            return new JSONObject(gson.toJson(object));
        } catch (JSONException e) {
            return null;
        }
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
