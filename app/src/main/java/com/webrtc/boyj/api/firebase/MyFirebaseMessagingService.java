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
    }

    @Override
    public void onNewToken(String token) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(UserRepositoryImpl.FIELD_USER_TOKEN, token);
        edit.putBoolean(UserRepositoryImpl.CHANGED, true);
        edit.apply();
    }

    private void handleNow(String room) {

    }

    private void sendRegistrationToServer(String token) {

    }
}