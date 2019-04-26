package com.webrtc.boyj.data.source;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.remote.response.UserResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface UserDataSource {

    @NonNull
    Single<UserResponse> getProfile(@NonNull final String id);

    @NonNull
    Single<List<User>> getOtherUserList(@NonNull final String id);

    @NonNull
    Completable registerUser(@NonNull final User user);

    @NonNull
    Completable updateDeviceToken(@NonNull final String id, @NonNull final String token);

    @NonNull
    Completable updateUserName(@NonNull final String id, @NonNull final String name);
}
