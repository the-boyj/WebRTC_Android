package com.webrtc.boyj.data.source.remote;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserDataSource;
import com.webrtc.boyj.data.source.remote.response.StatusCode;
import com.webrtc.boyj.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
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

    @NonNull
    @Override
    public Single<User> getProfile(@NonNull String id) {
        return boyjApi.getProfile(id)
                .doOnSuccess(response -> Logger.ii("getProfile", response.toString()))
                .flatMap(response -> {
                    if (response.getCode() == StatusCode.OK) {
                        return Single.just(response.getItem());
                    } else {
                        return Single.error(new IllegalStateException("getProfile Error"));
                    }
                }).subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable insertUserList(@NonNull List<User> userList) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public Single<List<User>> getOtherUserListExceptId(@NonNull String id) {
        return boyjApi.getOthers(id)
                .doOnSuccess(response -> Logger.ii("getOtherUserListExceptId", response.toString()))
                .map(response -> response.getItems() == null ?
                        new ArrayList<User>() :
                        response.getItems())
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<List<User>> getOtherUserListExceptIds(@NonNull List<String> ids) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public Completable registerUser(@NonNull User user) {
        return boyjApi.registerUser(user)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable updateDeviceToken(@NonNull String id,
                                         @NonNull String token) {
        return boyjApi.updateDeviceToken(id, token)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable updateUserName(@NonNull String id,
                                      @NonNull String name) {
        return boyjApi.updateUserName(id, name)
                .subscribeOn(Schedulers.io());
    }
}
