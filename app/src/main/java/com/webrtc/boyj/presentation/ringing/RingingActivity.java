package com.webrtc.boyj.presentation.ringing;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivityRingingBinding;
import com.webrtc.boyj.presentation.BaseActivity;

public class RingingActivity extends BaseActivity<ActivityRingingBinding> {
    // Todo : Connecting FCM
    public static Intent getLaunchIntent(@NonNull final Context context) {
        return new Intent(context, RingingActivity.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ringing;
    }
}
