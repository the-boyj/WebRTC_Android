package com.webrtc.boyj.presentation.ringing;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.R;
import com.webrtc.boyj.api.signalling.payload.FCMPayload;
import com.webrtc.boyj.databinding.ActivityRingingBinding;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.presentation.call.CallActivity;

public class RingingActivity extends BaseActivity<ActivityRingingBinding> {
    private static final String EXTRA_FCM_PAYLOAD = "EXTRA_FCM_PAYLOAD";
    private FCMPayload payload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        payload = (FCMPayload) getIntent().getSerializableExtra(EXTRA_FCM_PAYLOAD);

        initViews();
        initViewModel();
    }

    private void initViews() {
        findViewById(R.id.fab_accept).setOnClickListener(__ -> startCallActivity());
        findViewById(R.id.fab_reject).setOnClickListener(__ -> {
            binding.getVm().reject();
            finish();
        });
    }

    private void initViewModel() {
        final RingingViewModel vm = ViewModelProviders.of(this,
                new RingingViewModelFactory(payload.getTel())).get(RingingViewModel.class);
        binding.setVm(vm);
    }

    private void startCallActivity() {
        startActivity(CallActivity.getLaunchIntent(this, payload.getTel(), payload.getRoom(), false));
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
