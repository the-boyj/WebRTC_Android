package com.webrtc.boyj.model.dao;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.webrtc.boyj.api.firebase.DeviceTokenManager;
import com.webrtc.boyj.api.firebase.FireStoreManager;
import com.webrtc.boyj.model.dto.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.functions.Function;

public class UserDAO {
    private static String TAG="BOYJ_UserInfoDAO";
    public static String COLLECTION_NAME="User";
    public static String KEY_NAME="name";
    public static String KEY_TOKEN="deviceToken";
    public static String KEY_TEL="tel";

    public Single<String> create(String name){
        return DeviceTokenManager.getDeviceToken().flatMap((Function<String, Single<String>>) token -> {
            final Map<String, Object> userInfo = new HashMap<>();
            userInfo.put(KEY_NAME, name);
            userInfo.put(KEY_TOKEN, token);
            userInfo.put(KEY_TEL, "010-0000-0000");
            return FireStoreManager.createDocument(COLLECTION_NAME ,userInfo ,FireStoreManager.AUTO_CREATE_DOCUMENT_ID);
        });
    }

    public Single<List<User>> readAll(){
        return Single.create(emitter -> {
            final List<User> userList=new ArrayList<>();

            FireStoreManager.getCollection(COLLECTION_NAME).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        userList.add(new User(document.get("name").toString(),document.get("tel").toString(),document.get("deviceToken").toString()));
                    }
                    emitter.onSuccess(userList);
                }
                else {
                    emitter.onError(task.getException());
                }
            });
        });
    }
}
