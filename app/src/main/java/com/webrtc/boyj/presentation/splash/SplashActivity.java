package com.webrtc.boyj.presentation.splash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.webrtc.boyj.R;

import io.reactivex.disposables.Disposable;

public class SplashActivity extends AppCompatActivity {
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


    }
}
