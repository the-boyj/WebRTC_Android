package com.webrtc.boyj.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class User {
    @SerializedName("userId")
    @Expose
    private final String id;

    @Expose
    @SerializedName("name")
    private final String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static User createFromId(@NonNull final String id) {
        return new User(id, id);
    }

    public boolean isEmpty() {
        return id == null && name == null;
    }

    public static User emptyUser() {
        return new User(null, null);
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