package com.webrtc.boyj.api.firebase;


import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webrtc.boyj.model.dao.UserDAO;
import com.webrtc.boyj.view.activity.CallActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "BOYJ_MyFCMService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.d("FirebaseLog", "message received");
        String room=remoteMessage.getData().get("room");
        handleNow(room);
    }
    // [END receive_message]


    // [START on_new_token]
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    private void handleNow(String room) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra("caller",false);
        intent.putExtra("room",room);
        startActivity(intent);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        UserDAO.create("unknown").subscribe(s -> Log.d(TAG,"sendRegistrationToServer"));

    }


}