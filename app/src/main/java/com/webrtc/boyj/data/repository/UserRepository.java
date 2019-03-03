package com.webrtc.boyj.data.repository;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.firestore.response.UserResponse;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface UserRepository {
    @NonNull
    Single<UserResponse> getUserList(@NonNull final String tel);

    @NonNull
    Completable updateToken(@NonNull String tel);

    @NonNull
    Single<User> updateUserName(@NonNull final String tel,
                                @NonNull final String name);
}
