package com.webrtc.boyj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.repository.UserRepositoryImpl;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class LegacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legacy);

        User user = UserRepositoryImpl.getInstance(FirebaseFirestore.getInstance(),
                FirebaseInstanceId.getInstance())
                .getProfile("010-0000-0000")
                .observeOn(AndroidSchedulers.mainThread())
                .blockingGet();

        Log.d("Melon", user.getName());
        Log.d("Melon", user.getTel());
        Log.d("Melon", user.getDeviceToken());
    }
}
