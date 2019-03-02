package com.webrtc.boyj.presentation.callon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivityCallOnBinding;
import com.webrtc.boyj.presentation.BaseActivity;

public class CallOnActivity extends BaseActivity<ActivityCallOnBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Todo : Connecting FCM
    public static Intent getLaunchIntent(@NonNull final Context context) {
        return new Intent(context, CallOnActivity.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call_on;
    }
}
