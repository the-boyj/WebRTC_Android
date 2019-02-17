package com.webrtc.boyj.api.firebase;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tedpark.tedpermission.rx2.TedRx2Permission;
import com.webrtc.boyj.BuildConfig;
import com.webrtc.boyj.data.repository.UserRepository;
import com.webrtc.boyj.data.repository.UserRepositoryImpl;
import com.webrtc.boyj.utils.Logger;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "BOYJ_MyFCMService";
    private static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;

    @NonNull
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.d("FCM received");
        String room = remoteMessage.getData().get("room");
        // handleNow(room);
    }

    @SuppressLint("MissingPermission, HardwareIds")
    @Override
    public void onNewToken(String token) {
        final UserRepository repository = UserRepositoryImpl.getInstance(FirebaseFirestore.getInstance());

        disposable.add(TedRx2Permission.with(this)
                .setPermissions(READ_PHONE_STATE)
                .request()
                .flatMapCompletable(result -> {
                    if (result.isGranted()) {
                        TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        final String tel = mgr.getLine1Number().replace("+82", "0");
                        return repository.updateToken(tel, token);
                    } else {
                        return Completable.error(new IllegalStateException("Not Permission"));
                    }
                }).subscribe(() -> {
                    // Todo : Send broadcast to MainActivity.
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "Success update Token : " + token);
                    }
                }, e -> {
                    /* Todo : Error handling.*/
                    Log.d("Melon", e.toString());
                }));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}