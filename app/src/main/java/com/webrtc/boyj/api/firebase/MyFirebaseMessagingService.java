package com.webrtc.boyj.api.firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "BOYJ_MyFCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
    }

    @Override
    public void onNewToken(String token) {
        sendRegistrationToServer(token);
    }

    private void handleNow(String room) {
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
}