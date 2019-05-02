package com.webrtc.boyj.data.source;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;

import java.util.List;

import io.reactivex.Single;

public interface UserDataSource {

    @NonNull
    Single<User> getProfile(@NonNull final String id);

    @NonNull
    Single<List<User>> insertUserList(@NonNull final List<User> userList);

    @NonNull
    Single<List<User>> getOtherUserListExceptId(@NonNull final String id);

    @NonNull
    Single<List<User>> getOtherUserListExceptIds(@NonNull final List<String> ids);

    @NonNull
    Single<User> registerUser(@NonNull final User user);

    @NonNull
    Single<User> updateDeviceToken(@NonNull final String id, @NonNull final String token);

    @NonNull
    Single<User> updateUserName(@NonNull final String id, @NonNull final String name);
}
