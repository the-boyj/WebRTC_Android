package com.webrtc.boyj.data.source.firestore.response;

import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;

import java.util.List;

public class UserResponse {
    private List<User> otherUserList;
    private User user;

    public UserResponse() {
    }

    public List<User> getOtherUserList() {
        return otherUserList;
    }

    public void setOtherUserList(@NonNull List<User> otherUserList) {
        this.otherUserList = otherUserList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(@NonNull User user) {
        this.user = user;
    }
}
