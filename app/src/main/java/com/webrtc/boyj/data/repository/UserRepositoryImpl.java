package com.webrtc.boyj.data.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.webrtc.boyj.data.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class UserRepositoryImpl implements UserRepository {
    private static final String COLLECTION_USER = "user";
    private static final String FIELD_USER_TEL = "tel";
    private static final String FIELD_USER_NAME = "name";
    private static final String FIELD_USER_TOKEN = "token";
    private static final String NOT_EXIST_USER_NAME = "Unknown";

    private static volatile UserRepositoryImpl INSTANCE;

    public static UserRepository getInstance(@NonNull final FirebaseFirestore firestore,
                                             @NonNull final FirebaseInstanceId instanceId) {
        if (INSTANCE == null) {
            synchronized (UserRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserRepositoryImpl(firestore, instanceId);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    private final FirebaseFirestore firestore;
    @NonNull
    private final FirebaseInstanceId instanceId;

    private UserRepositoryImpl(@NonNull FirebaseFirestore firestore,
                               @NonNull FirebaseInstanceId instanceId) {
        this.firestore = firestore;
        this.instanceId = instanceId;
    }

    @NonNull
    @Override
    public Single<List<User>> getUserList(@NonNull String tel) {
        return Single.create((SingleOnSubscribe<List<User>>) emitter ->
                firestore.collection(COLLECTION_USER)
                        .get()
                        .addOnSuccessListener(snapshots -> {
                            final List<User> userList = new ArrayList<>();
                            for (final DocumentSnapshot snapshot : snapshots) {
                                final User user = snapshot.toObject(User.class);
                                if (user == null) {
                                    emitter.onError(new IllegalArgumentException("User is null"));
                                    return;
                                } else {
                                    if (user.getTel().equals(tel)) continue;
                                    userList.add(user);
                                }
                            }
                            emitter.onSuccess(userList);
                        })).subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<User> getProfile(@NonNull String tel) {
        final DocumentReference docRef = firestore.collection(COLLECTION_USER).document(tel);

        return getMyToken().flatMap(token ->
                Single.create((SingleOnSubscribe<User>) emitter ->
                        firestore.runTransaction(transaction -> {
                            final DocumentSnapshot snapshot = transaction.get(docRef);
                            User user;
                            if (!transaction.get(docRef).exists()) {
                                Log.d("Melon", "AAAA");
                                user = new User(NOT_EXIST_USER_NAME, tel, token);
                                transaction.set(docRef, user);
                            } else {
                                user = snapshot.toObject(User.class);
                                transaction.update(docRef, FIELD_USER_TOKEN, token);
                            }
                            return user;
                        })
                                .addOnSuccessListener(emitter::onSuccess)
                                .addOnFailureListener(emitter::onError)))
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable updateUserName(@NonNull String tel, @NonNull String name) {
        return Completable.create(emitter -> {
            firestore.collection(COLLECTION_USER);
        });
    }

    @NonNull
    private Single<String> getMyToken() {
        return Single.create((SingleOnSubscribe<String>) emitter -> instanceId.getInstanceId()
                .addOnSuccessListener(result -> emitter.onSuccess(result.getToken()))
                .addOnFailureListener(emitter::onError)
        ).subscribeOn(Schedulers.io());
    }
}
