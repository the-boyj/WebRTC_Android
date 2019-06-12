package com.webrtc.boyj.data.source;

import androidx.annotation.NonNull;

import com.webrtc.boyj.data.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface UserRepository {

    @NonNull
    Single<User> getProfile(@NonNull final String id);

    @NonNull
    Single<List<User>> loadNewUserListExceptId(@NonNull final String id);

    @NonNull
    Single<List<User>> getOtherUserListExceptId(@NonNull final String id);

    @NonNull
    Completable registerUser(@NonNull final User user);

    @NonNull
    Completable updateDeviceToken(@NonNull final String id);

    @NonNull
    Completable updateUserName(@NonNull final String id,
                               @NonNull final String name);
}
