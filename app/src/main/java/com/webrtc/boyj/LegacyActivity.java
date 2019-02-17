package com.webrtc.boyj;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LegacyActivity extends AppCompatActivity {
    private static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;

    @SuppressLint({"MissingPermission", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legacy);
    }
}
