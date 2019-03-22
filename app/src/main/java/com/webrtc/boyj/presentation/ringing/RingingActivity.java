package com.webrtc.boyj.presentation.ringing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.R;
import com.webrtc.boyj.api.signalling.payload.FCMPayload;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.databinding.ActivityRingingBinding;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.presentation.call.CallActivity;

public class RingingActivity extends BaseActivity<ActivityRingingBinding> {

    private static final String EXTRA_FCM_PAYLOAD = "EXTRA_FCM_PAYLOAD";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Todo : get User information as FCM
        initViews();
    }

    private void initViews() {
        findViewById(R.id.fab_accept).setOnClickListener(__ -> startCallActivity());
        findViewById(R.id.fab_reject).setOnClickListener(__ -> finish());
    }

    private void startCallActivity() {
        // Mock data
        final User otherUser = new User("010-2222-3333", "홍길동", "10129129");

        startActivity(CallActivity.getLaunchIntent(this,
                otherUser, null, false));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }


    public static Intent getLaunchIntent(@NonNull final Context context,
                                         @NonNull final FCMPayload payload) {
        return getLaunchIntent(context, RingingActivity.class)
                .putExtra(EXTRA_FCM_PAYLOAD, payload);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_ringing;
    }
}
