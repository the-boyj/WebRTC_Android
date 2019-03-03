package com.webrtc.boyj.presentation.ringing;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.databinding.ActivityRingingBinding;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.presentation.call.CallActivity;

public class RingingActivity extends BaseActivity<ActivityRingingBinding> {

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ringing;
    }
}
