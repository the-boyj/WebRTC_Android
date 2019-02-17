package com.webrtc.boyj.data.repository;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.firestore.response.UserResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class UserRepositoryImpl implements UserRepository {
    private static final String COLLECTION_USER = "user";

    private static final String FIELD_USER_NAME = "name";
    public static final String FIELD_USER_TOKEN = "deviceToken";

    private static final String NOT_EXIST_USER_NAME = "Unknown";
    private static final String ERROR_USER_NOT_EXIST = "User is not exists";

    public static final String PREF_CHANGED = "CHANGED";

    private static volatile UserRepositoryImpl INSTANCE;

    public static UserRepository getInstance(@NonNull final FirebaseFirestore firestore,
                                             @NonNull final SharedPreferences pref) {
        if (INSTANCE == null) {
            synchronized (UserRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserRepositoryImpl(firestore, pref);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    private final FirebaseFirestore firestore;
    @NonNull
    private final SharedPreferences pref;

    private UserRepositoryImpl(@NonNull FirebaseFirestore firestore,
                               @NonNull SharedPreferences pref) {
        this.firestore = firestore;
        this.pref = pref;
    }

    @NonNull
    @Override
    public Single<UserResponse> getUserList(@NonNull String tel) {
        return Single.create((SingleOnSubscribe<UserResponse>) emitter ->
                firestore.collection(COLLECTION_USER)
                        .get()
                        .addOnSuccessListener(snapshots -> {
                            final List<User> userList = new ArrayList<>();
                            final UserResponse response = new UserResponse();
                            for (final DocumentSnapshot snapshot : snapshots) {
                                final User user = snapshot.toObject(User.class);
                                if (user == null) {
                                    emitter.onError(new IllegalArgumentException(ERROR_USER_NOT_EXIST));
                                    return;
                                } else {
                                    if (user.getTel().equals(tel)) {
                                        response.setMyUser(user);
                                        continue;
                                    }
                                    userList.add(user);
                                    response.setUserList(userList);
                                }
                            }
                            emitter.onSuccess(response);
                        })).subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable updateToken(@NonNull final String tel) {
        final String token = pref.getString(FIELD_USER_TOKEN, null);
        if (token == null) {
            return Completable.error(new IllegalArgumentException("Token is not exists"));
        }
        final DocumentReference docRef = firestore.collection(COLLECTION_USER).document(tel);

        if (pref.getBoolean(PREF_CHANGED, false)) {
            return Completable.create(emitter ->
                    firestore.runTransaction(transaction -> {
                        if (!transaction.get(docRef).exists()) {
                            transaction.set(docRef, new User(NOT_EXIST_USER_NAME, tel, token));
                        } else {
                            transaction.update(docRef, FIELD_USER_TOKEN, token);
                        }
                        return null;
                    }).addOnSuccessListener(__ -> {
                        pref.edit().putBoolean(PREF_CHANGED, true).apply();
                        emitter.onComplete();
                    }).addOnFailureListener(emitter::onError)).subscribeOn(Schedulers.io());
        } else {
            return Completable.complete();
        }
    }

    @NonNull
    @Override
    public Single<User> updateUserName(@NonNull String tel, @NonNull String name) {
        final Map<String, Object> map = new HashMap<>();
        map.put(FIELD_USER_NAME, name);

        return Completable.create(emitter ->
                firestore.collection(COLLECTION_USER)
                        .document(tel)
                        .update(FIELD_USER_NAME, map)
                        .addOnSuccessListener(__ -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)).subscribeOn(Schedulers.io())
                .andThen(Single.create((SingleOnSubscribe<User>) emitter ->
                        firestore.collection(COLLECTION_USER)
                                .document(tel)
                                .get()
                                .addOnSuccessListener(snapshot -> {
                                    final User user = snapshot.toObject(User.class);
                                    if (user == null) {
                                        emitter.onError(new IllegalArgumentException(ERROR_USER_NOT_EXIST));
                                    } else {
                                        emitter.onSuccess(user);
                                    }
                                }).addOnFailureListener(emitter::onError))).subscribeOn(Schedulers.io());
    }
}
