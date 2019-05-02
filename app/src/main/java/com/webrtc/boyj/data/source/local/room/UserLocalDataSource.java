package com.webrtc.boyj.data.source.local.room;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserDataSource;
import com.webrtc.boyj.data.source.local.room.dao.UserDao;
import com.webrtc.boyj.data.source.local.room.entity.UserEntity;
import com.webrtc.boyj.data.source.local.room.entity.UserMapper;

import java.util.ArrayList;
import java.util.List;

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
    public Single<List<User>> insertUserList(@NonNull List<User> userList) {
        return null;
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
    public Single<User> registerUser(@NonNull User user) {
        final UserEntity entity = UserMapper.toEntityFromUser(user);
        return Single.fromCallable(() -> {
            userDao.insert(entity);
            return user;
        }).subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Single<User> updateDeviceToken(@NonNull String id, @NonNull String token) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public Single<User> updateUserName(@NonNull String id, @NonNull String name) {
        final UserEntity entity = new UserEntity(id, name);
        return Single.fromCallable(() -> {
            userDao.insert(entity);
            return new User(id, name);
        }).subscribeOn(Schedulers.io());
    }
}