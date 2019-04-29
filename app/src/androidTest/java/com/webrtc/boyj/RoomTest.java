package com.webrtc.boyj;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.local.room.AppDatabase;
import com.webrtc.boyj.data.source.local.room.dao.UserDao;
import com.webrtc.boyj.data.source.local.room.entity.UserEntity;
import com.webrtc.boyj.utils.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;

@FixMethodOrder
@RunWith(AndroidJUnit4.class)
public class RoomTest {
    private UserDao userDao;
    private AppDatabase db;

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.userDao();
    }

    @After
    public void close() {
        db.close();
    }

    @Test
    public void registerUserTest() {
        final UserEntity entity = new UserEntity("id", "name");
        userDao.insert(entity);
        try {
            User user = Single.fromCallable(() -> {
                final UserEntity u = userDao.selectById("AA");
                if (u == null) {
                    return new User(null, null);
                } else {
                    return new User(u.getId(), u.getName());
                }
            }).blockingGet();
            Logger.i(user.getName());

        } catch (Exception e) {
            e.printStackTrace();
            Logger.i(e.toString());
        }
        /*Logger.i(user.toString());
        Logger.i(user.getId());
        Logger.i(user.getName());
        Assert.assertEquals("id", user.getId());*/
    }

    @Test
    public void getOthersExceptIdTest() {
        for (int i = 0; i < 10; i++) {
            userDao.insert(new UserEntity("ID-" + i, "NAME-" + i));
        }
        final String id = "ID-3";
        final List<UserEntity> entities = userDao.selectExceptId(id);
        for (UserEntity e : entities) {
            Logger.i(e.getId() + " | " + e.getName());
        }
    }

    @Test
    public void getOthersExceptIdsTest() {
        for (int i = 0; i < 10; i++) {
            userDao.insert(new UserEntity("IDS-" + i, "NAME-" + i));
        }
        final List<String> ids = Arrays.asList("IDS-1", "IDS-4", "IDS-7");
        final List<UserEntity> entities = userDao.selectExceptIds(ids);
        for (UserEntity e : entities) {
            Logger.i(e.getId() + " | " + e.getName());
        }
    }
}
