package com.webrtc.boyj.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class User {
    @SerializedName("userId")
    @Expose
    private String id;

    @Expose
    @SerializedName("name")
    private String name;

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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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