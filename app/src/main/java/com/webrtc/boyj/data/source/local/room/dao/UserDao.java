package com.webrtc.boyj.data.source.local.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Update
    Completable update(final UserEntity entity);
}
