package com.webrtc.boyj.data.source.local.room.entity;

import androidx.annotation.NonNull;

import com.webrtc.boyj.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static User toUserFromEntity(@NonNull final UserEntity entity) {
        return new User(entity.getId(), entity.getName());
    }

    public static UserEntity toEntityFromUser(@NonNull final User user) {
        return new UserEntity(user.getId(), user.getName());
    }

    public static List<User> toUserListFromEntities(@NonNull final List<UserEntity> entities) {
        final List<User> list = new ArrayList<>();
        for (UserEntity entity : entities) {
            list.add(toUserFromEntity(entity));
        }
        return list;
    }

    public static List<UserEntity> toEntitiesFromUserList(@NonNull final List<User> userList) {
        final List<UserEntity> entities = new ArrayList<>();
        for (User user : userList) {
            entities.add(toEntityFromUser(user));
        }
        return entities;
    }
}
