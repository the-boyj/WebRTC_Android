package com.webrtc.boyj.data.source;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface UserRepository {

    @NonNull
    Single<User> getProfile(@NonNull final String id);

    @NonNull
    Single<List<User>> getOtherUserListExceptId(@NonNull final String id);

    @NonNull
    Single<List<User>> getOtherUserListExceptIds(@NonNull final List<String> ids);

    @NonNull
    Single<User> registerUser(@NonNull final User user);

    @NonNull
    Completable updateDeviceToken(@NonNull final String id);

    @NonNull
    Single<User> updateUserName(@NonNull final String id, @NonNull final String name);
}
