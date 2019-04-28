package com.webrtc.boyj.data.source;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.remote.response.Response;
import com.webrtc.boyj.data.source.remote.response.UserItem;

import java.util.List;

import io.reactivex.Single;

public interface UserDataSource {

    @NonNull
    Single<Response<UserItem>> getProfile(@NonNull final String id);

    @NonNull
    Single<List<User>> getOtherUserList(@NonNull final String id);

    @NonNull
    Single<User> registerUser(@NonNull final User user);

    @NonNull
    Single<User> updateDeviceToken(@NonNull final String id, @NonNull final String token);

    @NonNull
    Single<User> updateUserName(@NonNull final String id, @NonNull final String name);
}
