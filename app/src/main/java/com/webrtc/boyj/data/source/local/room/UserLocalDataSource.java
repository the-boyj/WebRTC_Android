package com.webrtc.boyj.data.source.local.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserDataSource;
import com.webrtc.boyj.data.source.local.room.dao.UserDao;
import com.webrtc.boyj.data.source.local.room.entity.UserEntity;
import com.webrtc.boyj.data.source.local.room.entity.UserMapper;
import com.webrtc.boyj.utils.Logger;

import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class UserLocalDataSource implements UserDataSource {
    @NonNull
    private final UserDao userDao;
    @Nullable
    private static volatile UserDataSource INSTANCE;

    private UserLocalDataSource(@NonNull final UserDao userDao) {
        this.userDao = userDao;
    }

    public static UserDataSource getInstance(@NonNull final UserDao userDao) {
        if (INSTANCE == null) {
            synchronized (UserLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserLocalDataSource(userDao);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public Single<User> getProfile(@NonNull String id) {
        return userDao.selectById(id)
                .doOnSuccess(entity -> Logger.ii(entity.getId(), entity.getName()))
                .subscribeOn(Schedulers.io())
                .map(UserMapper::toUserFromEntity);
    }

    @NonNull
    @Override
    public Completable insertUserList(@NonNull List<User> userList) {
        return userDao.insertAll(UserMapper.toEntitiesFromUserList(userList))
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable deleteUserListExceptId(@NonNull String id) {
        return userDao.deleteExceptId(id)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<List<User>> getOtherUserListExceptId(@NonNull String id) {
        return userDao.selectExceptId(id)
                .subscribeOn(Schedulers.io())
                .doOnSuccess(entities -> Logger.i(entities.toString()))
                .onErrorReturnItem(Collections.emptyList())
                .map(UserMapper::toUserListFromEntities);
    }

    @NonNull
    @Override
    public Completable registerUser(@NonNull User user, @Nullable String deviceToken) {
        final UserEntity entity = UserMapper.toEntityFromUser(user);
        return userDao.insert(entity).subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable updateDeviceToken(@NonNull String id, @NonNull String token) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public Completable updateUserName(@NonNull String id, @NonNull String name) {
        final UserEntity entity = new UserEntity(id, name);
        return userDao.update(entity).subscribeOn(Schedulers.io());
    }
}