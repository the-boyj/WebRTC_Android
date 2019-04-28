package com.webrtc.boyj;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.remote.BoyjApi;
import com.webrtc.boyj.data.source.remote.BoyjApiClient;
import com.webrtc.boyj.data.source.remote.response.UserItem;
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
        final Response<UserItem> response = boyjApi.getProfile(userId).blockingGet();
        final User oldUser = toUser(response);

        final Response<UserItem> updateResponse =
                boyjApi.updateUserName(userId, "변경 후").blockingGet();
    }

    private User toUser(Response<UserItem> response) {
        final UserItem item = response.getItem();
        return new User(item.getUserId(), item.getUserName());
    }

    @Test
    public void userResponseTest() {
        final Response response = new Response<>(200,
                new UserItem("userId", "userName"));
        System.out.println(response);
    }

    @Test
    public void userListResponseTest() {
        final ListResponse response = new ListResponse<>(200,
                Arrays.asList(
                        new UserItem("user1", "오석현"),
                        new UserItem("user2", "윤영직"),
                        new UserItem("user3", "변현우")
                ));
        System.out.println(response);
    }

}
