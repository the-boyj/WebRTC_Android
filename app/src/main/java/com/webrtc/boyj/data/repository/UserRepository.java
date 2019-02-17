package com.webrtc.boyj.data.repository;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface UserRepository {
    // 자신을 제외한 유저 목록 반환
    @NonNull
    Single<List<User>> getUserList(@NonNull final String myToken);

    // 해당 토큰으로 등록한 유저가 있는지 확인
    @NonNull
    Maybe<User> getUser(@NonNull final String deviceToken);

    // 새로운 유저 등록
    @NonNull
    Completable addNewUser(@NonNull final String deviceToken,
                           @NonNull final String name);

    // 자신의 이름 변경
    @NonNull
    Completable updateUserName(@NonNull final String myToken,
                               @NonNull final String name);
}
