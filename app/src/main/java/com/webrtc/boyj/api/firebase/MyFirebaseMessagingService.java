package com.webrtc.boyj.api.firebase;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webrtc.boyj.data.repository.UserRepositoryImpl;
import com.webrtc.boyj.utils.Logger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "BOYJ_MyFCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.d("FCM received");
        String room = remoteMessage.getData().get("room");
        handleNow(room);
    }

    @Override
    public void onNewToken(String token) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit()
                .putString(UserRepositoryImpl.FIELD_USER_TOKEN, token)
                .putBoolean(UserRepositoryImpl.CHANGED, true)
                .apply();
    }

    private void handleNow(String room) {

    }
}