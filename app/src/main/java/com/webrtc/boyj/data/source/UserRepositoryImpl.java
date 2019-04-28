package com.webrtc.boyj.data.source;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.remote.response.StatusCode;
import com.webrtc.boyj.data.source.remote.response.UserItem;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@SuppressWarnings("SpellCheckingInspection")
public class UserRepositoryImpl implements UserRepository {
    @NonNull
    private final UserDataSource remoteDataSource;
    @NonNull
    private final TokenDataSource tokenDataSource;

    private static volatile UserRepositoryImpl INSTANCE;

    public static UserRepository getInstance(@NonNull final UserDataSource remoteDataSource,
                                             @NonNull TokenDataSource tokenDataSource) {
        if (INSTANCE == null) {
            synchronized (UserRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserRepositoryImpl(remoteDataSource, tokenDataSource);
                }
            }
        }
        return INSTANCE;
    }

    private UserRepositoryImpl(@NonNull UserDataSource remoteDataSource,
                               @NonNull TokenDataSource tokenDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.tokenDataSource = tokenDataSource;
    }

    /**
     * id 유저 정보를 발행한다.
     * 만약 유저 정보가 없다면 registerUser() 호출 후 해당 정보 발행한다.
     */
    @NonNull
    @Override
    public Single<User> getProfile(@NonNull String id) {
        return remoteDataSource.getProfile(id)
                .flatMap(response -> {
                    if (response.getCode() == StatusCode.OK) {
                        final UserItem item = response.getItem();
                        final User user = new User(item.getUserId(), item.getUserName());
                        return Single.just(user);
                    } else {
                        return registerUser(id);
                    }
                });
    }

    /**
     * id를 제외한 유저 리스트를 발행한다.
     */
    @NonNull
    @Override
    public Single<List<User>> getOtherUserList(@NonNull String id) {
        return remoteDataSource.getOtherUserList(id);
    }

    /**
     * getProfile()를 호출 후 유저 정보가 없는 경우 등록 진행
     * User.createFromId(id)를 통해 새로운 유저를 생성하고, 이를 서버에 등록한다.
     * 서버에 등록이 끝난 이후 생성된 정보를 발행한다.
     */
    @NonNull
    @Override
    public Single<User> registerUser(@NonNull final String id) {
        final User user = User.createFromId(id);
        return remoteDataSource.registerUser(user);
    }

    /**
     * id 유저의 Device Token 업데이트 이후 결과 반환
     * getProfile() 이후에 호출
     * tokenDataSource의 isNoewToken을 통해 토큰을 새로 등록할지 여부를 확인한다.
     */
    @NonNull
    @Override
    public Completable updateDeviceToken(@NonNull String id) {
        if (tokenDataSource.isNewToken()) {
            tokenDataSource.unsetNewToken();
            return remoteDataSource.updateDeviceToken(id, tokenDataSource.getToken()).ignoreElement();
        } else {
            return Completable.complete();
        }
    }

    /**
     * id 유저의 이름을 name 으로 변경 및 서버에 등록
     */
    @NonNull
    @Override
    public Single<User> updateUserName(@NonNull final String id,
                                       @NonNull final String name) {
        return remoteDataSource.updateUserName(id, name);
    }
}
