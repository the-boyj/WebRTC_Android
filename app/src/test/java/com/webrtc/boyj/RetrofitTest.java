package com.webrtc.boyj;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.remote.BoyjApi;
import com.webrtc.boyj.data.source.remote.BoyjApiClient;
import com.webrtc.boyj.data.source.remote.response.UserListResponse;
import com.webrtc.boyj.data.source.remote.response.UserResponse;

import org.junit.Before;
import org.junit.Test;

public class RetrofitTest {
    private BoyjApi boyjApi;

    @Before
    public void setUp() {
        boyjApi = BoyjApiClient.getInstance();
    }

    @Test(expected = RuntimeException.class)
    public void getProfileTest() {
        final String userId = "한글%&?=";
        final UserResponse response = boyjApi.getProfile(userId).blockingGet();
    }

    @Test(expected = RuntimeException.class)
    public void getOthersTest() {
        final String userId = "AAA";
        final UserListResponse response = boyjApi.getOthers(userId).blockingGet();
    }

    @Test(expected = RuntimeException.class)
    public void registerUserTest() {
        final User user = new User("아이디", "이름");
        final UserResponse response = boyjApi.registerUser(user).blockingGet();
    }

    @Test(expected = RuntimeException.class)
    public void updateDeviceTokenTest() {
        final String userId = "AAA";
        final String token = "SSS";
        final UserResponse response = boyjApi.updateDeviceToken(userId, token).blockingGet();
    }
}
