package com.webrtc.boyj.data.repository;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.firestore.response.UserResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("SpellCheckingInspection")
public class UserRepositoryImpl implements UserRepository {
    private static final String COLLECTION_USER = "user";

    private static final String FIELD_USER_NAME = "name";
    public static final String FIELD_USER_TOKEN = "deviceToken";
    public static final String CHANGED = "CHANGED";

    private static final String UNKNOWN = "Unknown";
    private static final String ERROR_USER_NOT_EXIST = "User is not exists";
    private static final String ERROR_TOKEN_NOT_EXIST = "Token is not exists";

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

        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        this.firestore.setFirestoreSettings(settings);
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
                                } else if (user.getTel().equals(tel)) {
                                    response.setUser(user);
                                } else {
                                    userList.add(user);
                                }
                            }
                            response.setOtherUserList(userList);
                            if (response.getUser() == null) {
                                emitter.onError(new IllegalArgumentException(ERROR_USER_NOT_EXIST));
                            } else {
                                emitter.onSuccess(response);
                            }
                        })).subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable updateToken(@NonNull final String tel) {
        final String token = pref.getString(FIELD_USER_TOKEN, null);
        final boolean isChanged = pref.getBoolean(CHANGED, false);
        if (token == null) {
            return Completable.error(new IllegalArgumentException(ERROR_TOKEN_NOT_EXIST));
        }
        final DocumentReference docRef = firestore.collection(COLLECTION_USER).document(tel);

        if (isChanged) {
            return Completable.create(emitter ->
                    firestore.runTransaction(transaction -> {
                        if (!transaction.get(docRef).exists()) {
                            transaction.set(docRef, new User(tel, UNKNOWN, token));
                        } else {
                            transaction.update(docRef, FIELD_USER_TOKEN, token);
                        }
                        return null;
                    }).addOnSuccessListener(__ -> {
                        pref.edit()
                                .putBoolean(CHANGED, false)
                                .apply();
                        emitter.onComplete();
                    }).addOnFailureListener(emitter::onError)).subscribeOn(Schedulers.io());
        } else {
            return Completable.complete();
        }
    }

    @NonNull
    @Override
    public Single<User> updateUserName(@NonNull final String tel,
                                       @NonNull final String name) {
        return Completable.create(emitter ->
                firestore.collection(COLLECTION_USER)
                        .document(tel)
                        .update(FIELD_USER_NAME, name)
                        .addOnSuccessListener(__ -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)).subscribeOn(Schedulers.io())
                .andThen(Single.create((SingleOnSubscribe<User>) emitter -> {
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
                            }).addOnFailureListener(emitter::onError);
                })).subscribeOn(Schedulers.io());
    }
}
