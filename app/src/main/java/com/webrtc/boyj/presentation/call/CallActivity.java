package com.webrtc.boyj.presentation.call;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivityCallBinding;
import com.webrtc.boyj.extension.custom.SplitLayout;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.utils.TelManager;

public class CallActivity extends BaseActivity<ActivityCallBinding> {
    private static final String CALLER_ID = "CALLER_ID";
    private static final String CALLEE_ID = "CALLEE_ID";
    private static final String EXTRA_IS_CALLER = "EXTRA_IS_CALLER";

    private CallViewModel vm;
    private BoyjAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final boolean isCaller = getIntent().getBooleanExtra(EXTRA_IS_CALLER, true);

        initViewModel();
        initViews();

        if (isCaller) {
            final String callerId = TelManager.getTelNumber(getApplicationContext());
            final String calleeId = getIntent().getStringExtra(CALLEE_ID);

            vm.createRoom(callerId);
            vm.dial(calleeId);
        } else {
            final String callerId = getIntent().getStringExtra(CALLER_ID);
            vm.accept(callerId);
        }
    }

    private void initViewModel() {
        vm = ViewModelProviders.of(this).get(CallViewModel.class);
        vm.init();
        binding.setVm(vm);
        subscribeViewModel();
    }

    private void subscribeViewModel() {
        vm.getIsEnded().observe(this, isEnded -> {
            if (Boolean.TRUE.equals(isEnded)) {
                finish();
            }
        });

        vm.getRemoteMediaStream().observe(this, mediaStream -> {
            if (mediaStream != null) {
                adapter.addMediaStream(mediaStream);
            }
        });

        vm.getRejectedUserName().observe(this, name -> {
            if (name != null) {
                adapter.removeMediaStreamfromId(name);
            }
        });

        vm.getByeUserName().observe(this, name -> {
            if (name != null) {
                adapter.removeMediaStreamfromId(name);
            }
        });
    }

    private void initViews() {
        initButton();
        initSplitLayout();
    }

    private void initButton() {
        findViewById(R.id.fab_reject).setOnClickListener(__ -> vm.hangUp());
        findViewById(R.id.iv_call_menu).setOnClickListener(__ ->
                CallMenuDialog.newInstance()
                        .show(getSupportFragmentManager(), "CallMenuDialog"));
    }

    private void initSplitLayout() {
        final SplitLayout splitLayout = findViewById(R.id.splitLayout);
        adapter = new BoyjAdapter(vm::endOfCall);
        splitLayout.setAdapter(adapter);
    }

    private void turnOnSpeaker() {
        AudioManager manager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (!manager.isSpeakerphoneOn()) {
            manager.setSpeakerphoneOn(true);
        }
    }

    /**
     * @param calleeId 상대방의 전화번호
     */
    @NonNull
    public static Intent getCallerLaunchIntent(@NonNull final Context context,
                                               @NonNull final String calleeId) {
        return getLaunchIntent(context, CallActivity.class)
                .putExtra(CALLEE_ID, calleeId)
                .putExtra(EXTRA_IS_CALLER, true);
    }

    @NonNull
    public static Intent getCalleeLaunchIntent(@NonNull final Context context,
                                               @NonNull final String callerId) {
        return getLaunchIntent(context, CallActivity.class)
                .putExtra(CALLER_ID, callerId)
                .putExtra(EXTRA_IS_CALLER, false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
    }
}
