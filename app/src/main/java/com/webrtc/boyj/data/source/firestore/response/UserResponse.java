package com.webrtc.boyj.data.source.firestore.response;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;

import java.util.List;

public class UserResponse {
    private List<User> userList;
    private User myProfile;

    public UserResponse() {
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(@NonNull List<User> userList) {
        this.userList = userList;
    }

    public User getMyProfile() {
        return myProfile;
    }

    public void setMyProfile(@NonNull User myProfile) {
        this.myProfile = myProfile;
    }
}
