package com.webrtc.boyj.data.source.firestore.response;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;

import java.util.List;

public class UserResponse {
    @NonNull
    private List<User> userList;
    @NonNull
    private User myUser;

    public UserResponse() {

    }

    @NonNull
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(@NonNull List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    public User getMyUser() {
        return myUser;
    }

    public void setMyUser(@NonNull User myUser) {
        this.myUser = myUser;
    }
}
