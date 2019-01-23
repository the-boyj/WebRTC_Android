package com.webrtc.boyj.model.dao;

import android.util.Log;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.webrtc.boyj.api.firebase.DeviceTokenManager;
import com.webrtc.boyj.api.firebase.FireStoreManager;
import com.webrtc.boyj.model.dto.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class UserDAO {
    private static String TAG="BOYJ_UserInfoDAO";
    public static String COLLECTION_NAME="User";
    public static String KEY_NAME="name";
    public static String KEY_TOKEN="deviceToken";
    public static String KEY_TEL="tel";

    /*
    private static UserDAO instance=null;

    private UserDAO(){

    }

    public static UserDAO getInstance(){
        if(instance==null){
            synchronized (instance){
                if(instance==null){
                    instance=new UserDAO();
                }
            }
        }

        return instance;
    }
*/


    public static Single<String> create(String name){

        return DeviceTokenManager.getDeviceToken().flatMap(new Function<String, Single<String>>() {
            @Override
            public Single<String> apply(String s) throws Exception {
                Map<String, Object> data = new HashMap<>();
                data.put(KEY_NAME, name);
                data.put(KEY_TOKEN, s);
                data.put(KEY_TEL, "010-0000-0000");
                return FireStoreManager.createDocument(COLLECTION_NAME, FireStoreManager.AUTO_CREATE_DOCUMENT_ID, data);

            }
        });

    }


    public static Single<List<User>> readAll(){
        return Single.create(emitter -> {
            List<User> userList=new ArrayList<>();

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
