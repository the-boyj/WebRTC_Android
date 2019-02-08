package com.webrtc.boyj.api.firebase;

import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class FireStoreManager {

    public static String AUTO_CREATE_DOCUMENT_ID="auto";
    public static String COMPLETE_STRING="complete";

    private static FirebaseFirestore db;

    static{

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    public static CollectionReference getCollection(@Nonnull String collection){
        return db.collection(collection);
    }

    public static Single<String> createDocument(@Nonnull String collection ,@Nonnull Map<String,Object> data ,@Nullable String documentId){
        final Task task;
        if(documentId == AUTO_CREATE_DOCUMENT_ID) {
            task = db.collection(collection).add(data);
        }
        else {
            task = db.collection(collection).document(documentId).set(data);
        }

        return Single.create(emitter -> task.addOnCompleteListener(task1 -> emitter.onSuccess(COMPLETE_STRING))
                .addOnFailureListener(e -> emitter.onError(e)));
    }
}
