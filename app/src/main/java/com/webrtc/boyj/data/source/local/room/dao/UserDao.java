package com.webrtc.boyj.data.source.local.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.webrtc.boyj.data.source.local.room.entity.UserEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface UserDao {
    @Query("SELECT * from users WHERE id=:id")
    Single<UserEntity> selectById(final String id);

    @Query("SELECT * from users WHERE id<>:id")
    Single<List<UserEntity>> selectExceptId(final String id);

    @Query("SELECT * from users WHERE id NOT IN(:ids)")
    Single<List<UserEntity>> selectExceptIds(final List<String> ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(final UserEntity user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(final List<UserEntity> userList);

    @Update
    Completable update(final UserEntity entity);
}
