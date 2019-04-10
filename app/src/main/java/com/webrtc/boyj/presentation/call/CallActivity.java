package com.webrtc.boyj.presentation.call;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.R;
import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.databinding.ActivityCallBinding;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.utils.TelManager;

public class CallActivity extends BaseActivity<ActivityCallBinding> {
    private static final String CALLEE_ID = "CALLEE_ID";
    private static final String EXTRA_IS_CALLER = "EXTRA_IS_CALLER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final boolean isCaller = getIntent().getBooleanExtra(EXTRA_IS_CALLER, true);

        initViews();
        initViewModel();

        if (isCaller) {
            final String callerId = TelManager.getTelNumber(getApplicationContext());
            final String calleeId = getIntent().getStringExtra(CALLEE_ID);

            assert callerId != null;

            binding.getVm().init();
            binding.getVm().createRoom(callerId);
        } else {
            binding.getVm().join();
        }
    }

    private void initViews() {
        findViewById(R.id.fab_reject).setOnClickListener(__ -> hangUp());
        findViewById(R.id.iv_call_menu).setOnClickListener(__ ->
                CallMenuDialog.newInstance()
                        .show(getSupportFragmentManager(), "CallMenuDialog"));
    }

    private void initViewModel() {
        final CallViewModel vm = ViewModelProviders.of(this,
                new CallViewModelFactory(new BoyjRTC())).get(CallViewModel.class);

        binding.setVm(vm);
    }

    private void turnOnSpeaker() {
        AudioManager manager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (!manager.isSpeakerphoneOn()) {
            manager.setSpeakerphoneOn(true);
        }
    }

    // Todo : Hangup handling
    private void hangUp() {
        binding.getVm().hangUp();
        finish();
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
    public static Intent getCalleeLaunchIntent(@NonNull final Context context) {
        return getLaunchIntent(context, CallActivity.class)
                .putExtra(EXTRA_IS_CALLER, false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
    }
}
