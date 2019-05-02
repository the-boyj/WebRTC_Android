package com.webrtc.boyj.data.source.local.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "users")
public class UserEntity {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final String id;

    @NonNull
    @ColumnInfo(name = "name")
    private final String name;

    public UserEntity(@NonNull String id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }
}
