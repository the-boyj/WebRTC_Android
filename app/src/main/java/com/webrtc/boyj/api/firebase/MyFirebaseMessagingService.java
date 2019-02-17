package com.webrtc.boyj.api.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
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
        Log.d("Melon", token);
/*        String myNumber;
        TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            myNumber = mgr.getLine1Number();
            myNumber = myNumber.replace("+82", "0");
        } catch(Exception e) { }*/
        // sendRegistrationToServer(token);
    }

    private void handleNow(String room) {
        // CallActivity.goToCallActivity(this, false, room);
    }

    private void sendRegistrationToServer(String token) {
        /*User user = new User();
        user.setDeviceToken(token);

        UserDAO userDAO = new UserDAO();
        userDAO.create(user)
                .subscribe(s -> Log.d(TAG, "sendRegistrationToServer"));*/
    }
}