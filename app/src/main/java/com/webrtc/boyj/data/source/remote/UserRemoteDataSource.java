package com.webrtc.boyj.data.source.remote;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserDataSource;
import com.webrtc.boyj.data.source.remote.response.ListResponse;
import com.webrtc.boyj.data.source.remote.response.Response;
import com.webrtc.boyj.data.source.remote.response.UserItem;

import io.reactivex.Single;

public class UserRemoteDataSource implements UserDataSource {
    private static volatile UserRemoteDataSource INSTANCE;

    public static UserDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (UserRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserRemoteDataSource();
                }
            }
        }
        return INSTANCE;
    }

    private UserRemoteDataSource() {

    }

    /**
     * id 정보에 해당하는 유저 정보를 서버로부터 받아온다.
     * 정보가 없는 경우 새로운 유저를 생성하여 등록한다.
     */
    @NonNull
    @Override
    public Single<Response<UserItem>> getProfile(@NonNull String id) {
        return null;
    }

    /**
     * id 정보에 해당하는 유저를 제외한 나머지 유저들의 목록을 서버로부터 받아온다.
     */
    @NonNull
    @Override
    public Single<ListResponse<UserItem>> getOtherUserList(@NonNull String id) {
        return null;
    }

    /**
     * 자신의 id에 해당하는 유저 정보가 서버에 없다면, 새로운 유저 정보를 등록한다.
     */
    @NonNull
    @Override
    public Single<Response<UserItem>> registerUser(@NonNull User user) {
        return null;
    }

    /**
     * id에 해당하는 유저의 디바이스토큰을 서버에 갱신한다.
     */
    @NonNull
    @Override
    public Single<Response<UserItem>> updateDeviceToken(@NonNull String id,
                                                        @NonNull String token) {
        return null;
    }

    /**
     * id에 해당하는 유저 정보의 이름을 변경 및 서버에 갱신한다.
     * 새로고침을 하기 위해서는 해당 API 호출 이후 getProfile()를 사용하거나,
     * 로컬 상에서 변경 후 핸들링하는 방법이 있다.
     */
    @NonNull
    @Override
    public Single<Response<UserItem>> updateUserName(@NonNull String id,
                                                     @NonNull String name) {
        return null;
    }
}
