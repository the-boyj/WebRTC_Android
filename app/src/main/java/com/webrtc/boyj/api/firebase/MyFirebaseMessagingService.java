package com.webrtc.boyj.api.firebase;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webrtc.boyj.data.repository.UserRepositoryImpl;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "BOYJ_MyFCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
    }

    @Override
    public void onNewToken(String token) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor edit = pref.edit();
        edit.putString(UserRepositoryImpl.FIELD_USER_TOKEN, token);
        edit.putBoolean(UserRepositoryImpl.CHANGED, true);
        edit.apply();
    }

    private void handleNow(String room) {

    }

    private void sendRegistrationToServer(String token) {

    }
}