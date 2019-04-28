package com.webrtc.boyj.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

public class User {
    private String id;
    private String name;

    public User() {
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static User createFromId(@NonNull final String id) {
        return new User(id, id);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o instanceof User) {
            User user = (User) o;
            return Objects.equals(id, user.id) &&
                    Objects.equals(name, user.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}