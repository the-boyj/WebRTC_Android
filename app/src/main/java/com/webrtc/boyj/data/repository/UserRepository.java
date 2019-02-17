package com.webrtc.boyj.data.repository;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface UserRepository {
    @NonNull
    Single<List<User>> getUserList(@NonNull final String tel);

    @NonNull
    Single<User> getProfile(@NonNull final String tel);

    @NonNull
    Completable updateUserName(@NonNull final String tel,
                               @NonNull final String name);
}
