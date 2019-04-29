package com.webrtc.boyj;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.remote.BoyjApi;
import com.webrtc.boyj.data.source.remote.BoyjApiClient;
import com.webrtc.boyj.data.source.remote.response.ListResponse;
import com.webrtc.boyj.data.source.remote.response.Response;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

public class RetrofitTest {
    private BoyjApi boyjApi;

    @Before
    public void setUp() {
        boyjApi = BoyjApiClient.getInstance();
    }

    @Test(expected = RuntimeException.class)
    public void getProfileTest() {
        final String userId = "한글%&?=";
        final Response response = boyjApi.getProfile(userId).blockingGet();
    }

    @Test(expected = RuntimeException.class)
    public void getOthersTest() {
        final String userId = "AAA";
        final ListResponse response = boyjApi.getOthers(userId).blockingGet();
    }

    @Test(expected = RuntimeException.class)
    public void registerUserTest() {
        final User user = new User("아이디", "이름");
        final Response response = boyjApi.registerUser(user).blockingGet();
    }

    @Test(expected = RuntimeException.class)
    public void updateDeviceTokenTest() {
        final String userId = "AAA";
        final String token = "SSS";
        final Response response = boyjApi.updateDeviceToken(userId, token).blockingGet();
    }

    @Test
    @Ignore
    public void updateUserNameTest() {
        final String userId = "test";
        final Response<User> response = boyjApi.getProfile(userId).blockingGet();

        final Response<User> updateResponse =
                boyjApi.updateUserName(userId, "변경 후").blockingGet();
    }

    @Test
    public void userResponseTest() {
        final Response<User> response = new Response<>(200, new User("userId", "userName"));
        System.out.println(response);
    }

    @Test
    public void userListResponseTest() {
        final ListResponse<User> response = new ListResponse<>(200,
                Arrays.asList(
                        new User("user1", "오석현"),
                        new User("user2", "윤영직"),
                        new User("user3", "변현우")
                ));
        System.out.println(response);
    }

}
