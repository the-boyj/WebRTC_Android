package com.webrtc.boyj.data.source.local.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
