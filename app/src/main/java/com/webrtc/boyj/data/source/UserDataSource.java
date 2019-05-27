package com.webrtc.boyj.data.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.webrtc.boyj.data.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface UserDataSource {

    @NonNull
    Single<User> getProfile(@NonNull final String id);

    @NonNull
    Completable
    insertUserList(@NonNull final List<User> userList);

    @NonNull
    Single<List<User>> getOtherUserListExceptId(@NonNull final String id);

    @NonNull
    Single<List<User>> getOtherUserListExceptIds(@NonNull final List<String> ids);

    @NonNull
    Completable
    registerUser(@NonNull final User user, @Nullable final String deviceToken);

    @NonNull
    Completable updateDeviceToken(@NonNull final String id, @NonNull final String token);

    @NonNull
    Completable updateUserName(@NonNull final String id, @NonNull final String name);
}
