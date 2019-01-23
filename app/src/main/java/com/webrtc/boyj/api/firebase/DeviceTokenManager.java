package com.webrtc.boyj.api.firebase;

import com.google.firebase.iid.FirebaseInstanceId;

import io.reactivex.Observable;
import io.reactivex.Single;

public class DeviceTokenManager {
    public static Single<String> getDeviceToken(){

        return Single.create(emitter -> {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                String token=task.getResult().getToken();
                emitter.onSuccess(token);
            })
            .addOnFailureListener(e -> {
                emitter.onError(e);
            });
        });


    }
}
