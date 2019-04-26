package com.webrtc.boyj.utils;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.webrtc.boyj.api.signalling.payload.Payload;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {
    @NonNull
    private static final Gson gson = new Gson();

    /**
     * Object를 JsonObject로 변환한다.
     *
     * @throws IllegalStateException 오브젝트를 JSONObject으로 변환할 수 없는 경우(JSONException) 발생
     */
    @NonNull
    public static JSONObject toJSONObject(@NonNull final Object object) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(gson.toJson(object));
        } catch (JSONException e) {
            throw new IllegalStateException("object is not valid");
        }
        return jsonObject;
    }

    @NonNull
    public static String toJson(@NonNull final Class<?> type,
                                @NonNull final Object object) {
        return String.format("%s %s", type.getSimpleName(), gson.toJson(object));
    }

    @NonNull
    public static <T extends Payload> T fromJson(@NonNull final Object object,
                                                 @NonNull final Class<T> type) {
        final JSONObject jsonObject = (JSONObject) object;
        return gson.fromJson(jsonObject.toString(), type);
    }
}
