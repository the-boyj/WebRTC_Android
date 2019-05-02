package com.webrtc.boyj.data.source.local.room.entity;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;

public class UserMapper {

    public static User toUserFromEntity(@NonNull final UserEntity entity) {
        return new User(entity.getId(), entity.getName());
    }

    public static UserEntity toEntityFromUser(@NonNull final User user) {
        return new UserEntity(user.getId(), user.getName());
    }
}
