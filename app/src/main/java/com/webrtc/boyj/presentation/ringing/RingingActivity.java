package com.webrtc.boyj.presentation.ringing;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.R;
import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.api.signalling.payload.AwakenPayload;
import com.webrtc.boyj.databinding.ActivityRingingBinding;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.presentation.call.CallActivity;
import com.webrtc.boyj.utils.TelManager;

public class RingingActivity extends BaseActivity<ActivityRingingBinding> {
    private static final String EXTRA_ROOM = "room";
    private static final String EXTRA_CALLER_ID = "callerId";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String room = getIntent().getStringExtra(EXTRA_ROOM);
        final String callerId = getIntent().getStringExtra(EXTRA_CALLER_ID);
        final String calleeId = TelManager.getTelNumber(getApplicationContext());

        initViews();
        initViewModel();

        binding.getVm().awaken(room, callerId, calleeId);
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
                new RingingViewModelFactory(new BoyjRTC())).get(RingingViewModel.class);
        binding.setVm(vm);
    }

    private void startCallActivity() {
        startActivity(CallActivity.getCalleeLaunchIntent(this));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    public static Intent getLaunchIntent(@NonNull final Context context,
                                         @NonNull final String room,
                                         @NonNull final String callerId) {
        return getLaunchIntent(context, RingingActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(EXTRA_ROOM, room)
                .putExtra(EXTRA_CALLER_ID, callerId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ringing;
    }

    @Override
    public void onBackPressed() {
        // disabled back press
    }
}
