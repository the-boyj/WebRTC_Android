package com.webrtc.boyj.api.firebase;

import com.google.firebase.iid.FirebaseInstanceId;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import io.reactivex.Single;

public class DeviceTokenManager {
    public static Single<String> getDeviceToken(){
        return Single.create(emitter -> FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(task -> {
                    final String token = task.getResult().getToken();
                    emitter.onSuccess(token);
                })
                //TODO Issue#10  ex) 네트워크 상태 , 종료 유도
                .addOnFailureListener(emitter::onError));
    }
}
