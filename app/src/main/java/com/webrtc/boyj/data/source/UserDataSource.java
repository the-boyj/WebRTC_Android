package com.webrtc.boyj.data.source;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.remote.response.ListResponse;
import com.webrtc.boyj.data.source.remote.response.Response;
import com.webrtc.boyj.data.source.remote.response.UserItem;

import io.reactivex.Single;

public interface UserDataSource {

    @NonNull
    Single<Response<UserItem>> getProfile(@NonNull final String id);

    @NonNull
    Single<ListResponse<UserItem>> getOtherUserList(@NonNull final String id);

    @NonNull
    Single<Response<UserItem>> registerUser(@NonNull final User user);

    @NonNull
    Single<Response<UserItem>> updateDeviceToken(@NonNull final String id, @NonNull final String token);

    @NonNull
    Single<Response<UserItem>> updateUserName(@NonNull final String id, @NonNull final String name);
}
