package com.webrtc.boyj.api.firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webrtc.boyj.data.repository.UserRepositoryImpl;
import com.webrtc.boyj.presentation.ringing.RingingActivity;

import java.util.Objects;

@SuppressWarnings("SpellCheckingInspection")
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String KEY_ROOM = "room";
    private static final String KEY_CALLER_ID = "callerId";

    @Override
    public void onMessageReceived(@NonNull final RemoteMessage remoteMessage) {
        final String room = Objects.requireNonNull(remoteMessage.getData().get(KEY_ROOM));
        final String callerId = Objects.requireNonNull(remoteMessage.getData().get(KEY_CALLER_ID));

        final Intent intent = RingingActivity.getLaunchIntent(getApplicationContext(), room, callerId);
        startActivity(intent);
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