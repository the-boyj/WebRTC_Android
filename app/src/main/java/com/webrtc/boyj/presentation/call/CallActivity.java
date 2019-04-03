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
import com.webrtc.boyj.presentation.BaseActivity;

public class CallActivity extends BaseActivity<ActivityCallBinding> {
    private static final String EXTRA_TEL = "EXTRA_TEL";
    private static final String EXTRA_ROOM = "EXTRA_ROOM";
    private static final String EXTRA_IS_CALLER = "EXTRA_IS_CALLER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String tel = getIntent().getStringExtra(EXTRA_TEL);
        final String room = getIntent().getStringExtra(EXTRA_ROOM);
        final boolean isCaller = getIntent().getBooleanExtra(EXTRA_IS_CALLER, true);

        initViews();
        initViewModel(tel);

        if (isCaller) {
            binding.getVm().dial(room);
        } else {
            binding.getVm().join();
        }
    }

    private void initViews() {
        findViewById(R.id.fab_reject).setOnClickListener(__ -> hangUp());
    }

    private void turnOnSpeaker() {
        AudioManager manager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (!manager.isSpeakerphoneOn()) {
            manager.setSpeakerphoneOn(true);
        }
    }

    private void initViewModel(@NonNull final String tel) {
        final CallViewModel vm = ViewModelProviders.of(this,
                new CallViewModelFactory(tel)).get(CallViewModel.class);

        binding.setVm(vm);
    }

    // Todo : Hangup handling
    private void hangUp() {
        binding.getVm().hangUp();
        finish();
    }

    @NonNull
    public static Intent getLaunchIntent(@NonNull final Context context,
                                         @NonNull final String tel,
                                         @NonNull final String room,
                                         final boolean isCaller) {
        return getLaunchIntent(context, CallActivity.class)
                .putExtra(EXTRA_TEL, tel)
                .putExtra(EXTRA_ROOM, room)
                .putExtra(EXTRA_IS_CALLER, isCaller);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
    }
}
