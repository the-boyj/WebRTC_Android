package com.webrtc.boyj.data.source.local.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.webrtc.boyj.data.source.local.room.entity.UserEntity;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * from users WHERE id=:id")
    UserEntity selectById(final String id);

    @Query("SELECT * from users WHERE id<>:id")
    List<UserEntity> selectExceptId(final String id);

    @Query("SELECT * from users WHERE id NOT IN(:ids)")
    List<UserEntity> selectExceptIds(final List<String> ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(final UserEntity user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(final List<UserEntity> userList);

    @Update
    void update(final UserEntity entity);
}
