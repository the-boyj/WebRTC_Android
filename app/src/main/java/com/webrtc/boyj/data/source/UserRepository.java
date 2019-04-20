package com.webrtc.boyj.data.source;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface UserRepository {

    @NonNull
    Single<User> getUser(@NonNull final String id);

    @NonNull
    Single<List<User>> getOtherUserList(@NonNull final String id);

    @NonNull
    Single<User> registerUser(@NonNull final String id);

    @NonNull
    Completable updateDeviceToken(@NonNull final String id);

    @NonNull
    Completable updateUserName(@NonNull final String id,
                               @NonNull final String name);
}
