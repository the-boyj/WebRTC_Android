package com.webrtc.boyj.data.source.remote;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserDataSource;
import com.webrtc.boyj.data.source.remote.response.ListResponse;
import com.webrtc.boyj.data.source.remote.response.Response;
import com.webrtc.boyj.data.source.remote.response.UserItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class UserRemoteDataSource implements UserDataSource {
    private static volatile UserRemoteDataSource INSTANCE;

    @NonNull
    private final BoyjApi boyjApi;

    private UserRemoteDataSource(@NonNull final BoyjApi boyjApi) {
        this.boyjApi = boyjApi;
    }

    public static UserDataSource getInstance(@NonNull final BoyjApi boyjApi) {
        if (INSTANCE == null) {
            synchronized (UserRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserRemoteDataSource(boyjApi);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * id 정보에 해당하는 유저 정보를 서버로부터 받아온다.
     */
    @NonNull
    @Override
    public Single<Response<UserItem>> getProfile(@NonNull String id) {
        return boyjApi.getProfile(id).subscribeOn(Schedulers.io());
    }

    /**
     * id 정보에 해당하는 유저를 제외한 나머지 유저들의 목록을 서버로부터 받아온다.
     */
    @NonNull
    @Override
    public Single<List<User>> getOtherUserList(@NonNull String id) {
        return boyjApi.getOthers(id)
                .map(ListResponse::getItems)
                .map(items -> {
                    final List<User> userList = new ArrayList<>();
                    for (UserItem item : items) {
                        userList.add(new User(item.getUserId(), item.getUserName()));
                    }
                    return userList;
                }).subscribeOn(Schedulers.io());
    }

    /**
     * 새로운 유저를 등록하고 그 값을 가져온다.
     */
    @NonNull
    @Override
    public Single<User> registerUser(@NonNull User user) {
        return boyjApi.registerUser(user)
                .map(Response::getItem)
                .map(item -> new User(item.getUserId(), item.getUserName()))
                .subscribeOn(Schedulers.io());
    }

    /**
     * id에 해당하는 유저의 디바이스토큰을 서버에 갱신한다.
     */
    @NonNull
    @Override
    public Single<User> updateDeviceToken(@NonNull String id,
                                          @NonNull String token) {
        return boyjApi.updateDeviceToken(id, token)
                .map(Response::getItem)
                .map(item -> new User(item.getUserId(), item.getUserName()))
                .subscribeOn(Schedulers.io());
    }

    /**
     * id에 해당하는 유저 정보의 이름을 변경 및 서버에 갱신한다.
     */
    @NonNull
    @Override
    public Single<User> updateUserName(@NonNull String id,
                                       @NonNull String name) {
        return boyjApi.updateUserName(id, name)
                .map(Response::getItem)
                .map(item -> new User(item.getUserId(), item.getUserName()))
                .subscribeOn(Schedulers.io());
    }
}
