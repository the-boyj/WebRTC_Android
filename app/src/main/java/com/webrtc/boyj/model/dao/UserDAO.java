package com.webrtc.boyj.model.dao;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.webrtc.boyj.api.firebase.FireStoreManager;
import com.webrtc.boyj.model.dto.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class UserDAO {
    private static String TAG="BOYJ_UserInfoDAO";
    public static String COLLECTION_NAME="User";
    public static String KEY_NAME="name";
    public static String KEY_TOKEN="deviceToken";
    public static String KEY_TEL="tel";

    public Single<String> create(User user){
        return FireStoreManager.createDocument(COLLECTION_NAME ,user ,FireStoreManager.AUTO_CREATE_DOCUMENT_ID);
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
                    //TODO Issue#10 네트워크 확인 , 토스트메시지
                    emitter.onError(task.getException());
                }
            });
        });
    }
}
