package com.webrtc.boyj;

import android.content.Context;

import androidx.room.EmptyResultSetException;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.local.room.AppDatabase;
import com.webrtc.boyj.data.source.local.room.dao.UserDao;
import com.webrtc.boyj.data.source.local.room.entity.UserEntity;
import com.webrtc.boyj.data.source.local.room.entity.UserMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.TestObserver;

@FixMethodOrder
@RunWith(AndroidJUnit4.class)
public class RoomTest {

    private UserDao userDao;
    private AppDatabase db;

    @Before
    public void setUp() {
        final Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.userDao();
    }

    @After
    public void close() {
        db.close();
    }

    @Test
    public void register_and_get_profile_success_test() {
        final String id = "tbtzpdlql";
        final User user = User.createFromId(id);

        TestObserver<User> testObserver = TestObserver.create();
        TestObserver updateObserver = TestObserver.create();

        userDao.insert(UserMapper.toEntityFromUser(user))
                .subscribe(updateObserver);

        userDao.selectById(id)
                .map(UserMapper::toUserFromEntity)
                .subscribe(testObserver);

        updateObserver.assertComplete();
        testObserver.assertValue(user);
    }

    @Test
    public void get_profile_failed_empty_results_test() {
        final String id = "failed";

        TestObserver<User> observer = TestObserver.create();

        userDao.selectById(id)
                .map(UserMapper::toUserFromEntity)
                .subscribe(observer);

        observer.assertError(EmptyResultSetException.class);
    }

    @Test
    public void get_user_list_except_by_id_test() {
        final String exceptId = "tbtzpdlql";
        final User exceptUser = User.createFromId(exceptId);
        final List<UserEntity> entities = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            entities.add(UserMapper.toEntityFromUser(new User("ID - " + i, "NAME - " + i)));
        }
        final List<User> userList = UserMapper.toUserListFromEntities(entities);

        entities.add(UserMapper.toEntityFromUser(exceptUser));

        TestObserver<List<User>> observer = TestObserver.create();
        userDao.insertAll(entities)
                .andThen(userDao.selectExceptId(exceptId))
                .map(UserMapper::toUserListFromEntities)
                .subscribe(observer);

        observer.assertValue(userList);
    }

    @Test
    public void update_user_name_test() {
        final String id = "update";
        final User user = User.createFromId(id);

        TestObserver<User> getUserObserver = TestObserver.create();

        userDao.insert(UserMapper.toEntityFromUser(user))
                .andThen(userDao.selectById(id)
                        .map(UserMapper::toUserFromEntity))
                .subscribe(getUserObserver);

        getUserObserver.assertValue(user);


        TestObserver<User> updateObserver = TestObserver.create();

        final User newUser = new User(id, "newUpdate");

        userDao.update(UserMapper.toEntityFromUser(newUser))
                .andThen(userDao.selectById(id)
                        .map(UserMapper::toUserFromEntity))
                .subscribe(updateObserver);

        updateObserver.assertValue(newUser);
    }
}
