package com.webrtc.boyj.model;

import com.webrtc.boyj.model.dto.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class UserRepository {
    private static UserRepository userRepository = null;

    // TODO : Firebase remote DataSource 변수 추가

    private UserRepository() { }

    public static UserRepository getInstance() {
        if(userRepository == null) {
            synchronized (UserRepository.class) {
                if(userRepository == null) {
                    userRepository = new UserRepository();
                }
            }
        }
        return userRepository;
    }

    public Single<List<User>> getItems() {
        // TODO : Firebase로부터 유저리스트 가져오면 아래 수정

        List<User> list = new ArrayList<>();
        list.add(new User("오OO", "010-0000-0001"));
        list.add(new User("윤OO", "010-0000-0002"));
        list.add(new User("장OO", "010-0000-0003"));
        list.add(new User("변OO", "010-0000-0004"));

        return Single.just(list)
                .subscribeOn(Schedulers.newThread());
    }
}
