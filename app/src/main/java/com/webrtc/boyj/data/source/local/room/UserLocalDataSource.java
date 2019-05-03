package com.webrtc.boyj.data.source.local.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserDataSource;
import com.webrtc.boyj.data.source.local.room.dao.UserDao;
import com.webrtc.boyj.data.source.local.room.entity.UserEntity;
import com.webrtc.boyj.data.source.local.room.entity.UserMapper;

import java.util.ArrayList;
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
        return Single.fromCallable(() -> {
            final UserEntity entity = userDao.selectById(id);
            if (entity == null) {
                return User.emptyUser();
            } else {
                return UserMapper.toUserFromEntity(entity);
            }
        }).subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable insertUserList(@NonNull List<User> userList) {
        final List<UserEntity> entities = new ArrayList<>();
        for (final User user : userList) {
            entities.add(UserMapper.toEntityFromUser(user));
        }
        return Completable.fromAction(() -> userDao.insertAll(entities))
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<List<User>> getOtherUserListExceptId(@NonNull String id) {
        return Single.fromCallable(() -> userDao.selectExceptId(id))
                .map(entities -> {
                    final List<User> userList = new ArrayList<>();
                    for (UserEntity entity : entities) {
                        userList.add(UserMapper.toUserFromEntity(entity));
                    }
                    return userList;
                }).subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<List<User>> getOtherUserListExceptIds(@NonNull List<String> ids) {
        return Single.fromCallable(() -> userDao.selectExceptIds(ids))
                .map(entities -> {
                    final List<User> userList = new ArrayList<>();
                    for (UserEntity entity : entities) {
                        userList.add(UserMapper.toUserFromEntity(entity));
                    }
                    return userList;
                }).subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable registerUser(@NonNull User user) {
        final UserEntity entity = UserMapper.toEntityFromUser(user);
        return Completable.fromAction(() ->
                userDao.insert(entity)).subscribeOn(Schedulers.io());
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
        return Completable.fromAction(() -> userDao.insert(entity))
                .subscribeOn(Schedulers.io());
    }
}