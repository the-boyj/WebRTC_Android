package com.webrtc.boyj.data.source;

import androidx.annotation.NonNull;

import com.webrtc.boyj.data.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class UserRepositoryImpl implements UserRepository {
    @NonNull
    private final UserDataSource localDataSource;
    @NonNull
    private final UserDataSource remoteDataSource;
    @NonNull
    private final TokenDataSource tokenDataSource;

    private static volatile UserRepositoryImpl INSTANCE;

    public static UserRepository getInstance(@NonNull final UserDataSource localDataSource,
                                             @NonNull final UserDataSource remoteDataSource,
                                             @NonNull TokenDataSource tokenDataSource) {
        if (INSTANCE == null) {
            synchronized (UserRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserRepositoryImpl(
                            localDataSource,
                            remoteDataSource,
                            tokenDataSource);
                }
            }
        }
        return INSTANCE;
    }

    private UserRepositoryImpl(@NonNull UserDataSource localDataSource,
                               @NonNull UserDataSource remoteDataSource,
                               @NonNull TokenDataSource tokenDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.tokenDataSource = tokenDataSource;
    }

    @NonNull
    @Override
    public Single<User> getProfile(@NonNull String id) {
        return localDataSource.getProfile(id)
                .onErrorReturnItem(User.emptyUser())
                .flatMap(user -> user.isEmpty() ?
                        getAndSaveRemoteProfile(id) :
                        Single.just(user));
    }

    @NonNull
    private Single<User> getAndSaveRemoteProfile(@NonNull final String id) {
        return remoteDataSource.getProfile(id)
                .flatMap(user -> localDataSource.registerUser(user).toSingleDefault(user));
    }

    @NonNull
    @Override
    public Single<List<User>> loadNewUserListExceptId(@NonNull String id) {
        return remoteDataSource.getOtherUserListExceptId(id)
                .flatMap(users -> localDataSource.insertUserList(users).toSingleDefault(users));
    }

    @NonNull
    @Override
    public Single<List<User>> getOtherUserListExceptId(@NonNull String id) {
        return localDataSource.getOtherUserListExceptId(id)
                .flatMap(users -> users.isEmpty() ?
                        getAndSaveRemoteUserListExceptId(id) :
                        Single.just(users));
    }

    @NonNull
    private Single<List<User>> getAndSaveRemoteUserListExceptId(@NonNull final String id) {
        return remoteDataSource.getOtherUserListExceptId(id)
                .flatMap(users -> !users.isEmpty() ?
                        localDataSource.insertUserList(users).toSingleDefault(users) :
                        Single.just(users));
    }

    @NonNull
    @Override
    public Single<List<User>> getOtherUserListExceptIds(@NonNull List<String> ids) {
        return localDataSource.getOtherUserListExceptIds(ids);
    }

    @NonNull
    @Override
    public Completable registerUser(@NonNull final User user) {
        return localDataSource.registerUser(user)
                .concatWith(remoteDataSource.registerUser(user));
    }

    @NonNull
    @Override
    public Completable updateDeviceToken(@NonNull String id) {
        if (tokenDataSource.isNewToken()) {
            tokenDataSource.unsetNewToken();
            return remoteDataSource.updateDeviceToken(id, tokenDataSource.getToken());
        } else {
            return Completable.complete();
        }
    }

    @NonNull
    @Override
    public Completable updateUserName(@NonNull final String id,
                                      @NonNull final String name) {
        return localDataSource.updateUserName(id, name)
                .concatWith(remoteDataSource.updateDeviceToken(id, name));
    }
}
