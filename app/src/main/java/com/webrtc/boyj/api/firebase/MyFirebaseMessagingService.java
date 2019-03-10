package com.webrtc.boyj.api.firebase;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.api.signalling.SignalingClientFactory;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.signalling.payload.FCMPayload;
import com.webrtc.boyj.data.repository.UserRepositoryImpl;
import com.webrtc.boyj.utils.Logger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "BOYJ_MyFCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.d("FCM received");
        FCMPayload payload = new FCMPayload(remoteMessage);
        handleNow(payload);
    }

    @Override
    public void onNewToken(String token) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit()
                .putString(UserRepositoryImpl.FIELD_USER_TOKEN, token)
                .putBoolean(UserRepositoryImpl.CHANGED, true)
                .apply();
    }

    public void handleNow(FCMPayload payload) {
        String room = payload.getRoom();

        final SignalingClient signalingClient = SignalingClientFactory.getSignalingClient();
        final AwakenPayload awakenPayload = new AwakenPayload.Builder(room).build();

        signalingClient.emitAwaken(awakenPayload);
    }
}