package com.webrtc.boyj.api.firebase;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webrtc.boyj.model.dao.UserDAO;
import com.webrtc.boyj.model.dto.User;
import com.webrtc.boyj.utils.Logger;
import com.webrtc.boyj.view.activity.CallActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "BOYJ_MyFCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Logger.d("FCM received");
        String room=remoteMessage.getData().get("room");
        handleNow(room);
    }

    @Override
    public void onNewToken(String token) {
        sendRegistrationToServer(token);
    }

    private void handleNow(String room) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra("isCaller",false);
        intent.putExtra("room",room);
        startActivity(intent);
    }

    private void sendRegistrationToServer(String token) {
        User user = new User();
        user.setDeviceToken(token);

        UserDAO userDAO = new UserDAO();
        userDAO.create(user)
                .subscribe(s -> Log.d(TAG,"sendRegistrationToServer"));
    }
}