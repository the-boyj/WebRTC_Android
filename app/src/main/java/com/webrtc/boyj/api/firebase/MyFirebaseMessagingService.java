package com.webrtc.boyj.api.firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.FCMPayload;
import com.webrtc.boyj.data.repository.UserRepositoryImpl;
import com.webrtc.boyj.presentation.ringing.RingingActivity;
import com.webrtc.boyj.utils.App;
import com.webrtc.boyj.utils.Logger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @NonNull
    private static final String TAG = "BOYJ_MyFCMService";

    @Override
    public void onMessageReceived(@NonNull final RemoteMessage remoteMessage) {
        Logger.d("FCM received");

        final FCMPayload payload = new FCMPayload(remoteMessage);

        final BoyjRTC boyjRTC = new BoyjRTC();
        boyjRTC.knock().subscribe(() -> {
            final Intent intent = RingingActivity.getLaunchIntent(getApplicationContext(), payload);
            startActivity(intent);
        });

        final String room = payload.getRoom();
        final AwakenPayload awakenPayload = new AwakenPayload.Builder(room).build();
        boyjRTC.awaken(awakenPayload);
    }


    @Override
    public void onNewToken(@NonNull final String token) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit()
                .putString(UserRepositoryImpl.FIELD_USER_TOKEN, token)
                .putBoolean(UserRepositoryImpl.CHANGED, true)
                .apply();
    }

}