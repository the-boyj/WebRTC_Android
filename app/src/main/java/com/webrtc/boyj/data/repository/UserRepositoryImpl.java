package com.webrtc.boyj.data.repository;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.firestore.response.UserResponse;
import com.webrtc.boyj.data.source.preferences.TokenDataSource;

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

    private static final String ERROR_USER_NOT_EXIST = "User is not exists";

    @NonNull
    private final FirebaseFirestore firestore;
    @NonNull
    private final TokenDataSource tokenDataSource;

    private static volatile UserRepositoryImpl INSTANCE;

    public static UserRepository getInstance(@NonNull final FirebaseFirestore firestore,
                                             @NonNull TokenDataSource tokenDataSource) {
        if (INSTANCE == null) {
            synchronized (UserRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserRepositoryImpl(firestore, tokenDataSource);
                }
            }
        }
        return INSTANCE;
    }

    private UserRepositoryImpl(@NonNull FirebaseFirestore firestore,
                               @NonNull TokenDataSource tokenDataSource) {
        this.firestore = firestore;
        this.tokenDataSource = tokenDataSource;

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
        final DocumentReference docRef = firestore.collection(COLLECTION_USER).document(tel);

        if (tokenDataSource.isNewToken()) {
            return Completable.create(emitter ->
                    firestore.runTransaction(transaction -> {
                        final String token = tokenDataSource.getToken();

                        if (transaction.get(docRef).exists()) {
                            transaction.update(docRef, "deviceToken", token);
                        } else {
                            transaction.set(docRef, new User(tel, "UNKNOWN", token));
                        }
                        return null;
                    }).addOnSuccessListener(__ -> {
                        tokenDataSource.unsetNewToken();
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
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .andThen(Single.create((SingleOnSubscribe<User>) emitter ->
                        firestore.collection(COLLECTION_USER)
                                .document(tel)
                                .get()
                                .addOnSuccessListener(snapshot ->
                                        emitter.onSuccess(snapshot.toObject(User.class)))
                                .addOnFailureListener(emitter::onError)))
                .subscribeOn(Schedulers.io());
    }
}
