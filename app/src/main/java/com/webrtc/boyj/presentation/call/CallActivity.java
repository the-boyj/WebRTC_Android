package com.webrtc.boyj.presentation.call;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.webrtc.boyj.R;
import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.databinding.ActivityCallBinding;
import com.webrtc.boyj.extension.custom.SplitLayout;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.utils.App;
import com.webrtc.boyj.utils.TelManager;

public class CallActivity extends BaseActivity<ActivityCallBinding> {
    private static final String EXTRA_CALLER_ID = "EXTRA_CALLER_ID";
    private static final String EXTRA_CALLEE_ID = "EXTRA_CALLEE_ID";
    private static final String EXTRA_IS_CALLER = "EXTRA_IS_CALLER";

    private CallViewModel vm;
    private BoyjAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        final boolean isCaller = intent.getBooleanExtra(EXTRA_IS_CALLER, true);

        initViewModel();
        initViews();

        if (isCaller) {
            initCaller(intent);
        } else {
            initCallee(intent);
        }
    }

    private void initViewModel() {
        final BoyjRTC boyjRTC = new BoyjRTC();
        boyjRTC.initRTC(App.getContext());

        final CallViewModelFactory factory = new CallViewModelFactory(boyjRTC);
        vm = ViewModelProviders.of(this, factory).get(CallViewModel.class);
        binding.setVm(vm);
        subscribeViewModel();
    }

    private void subscribeViewModel() {
        vm.getIsEnded().observe(this, isEnded -> {
            if (Boolean.TRUE.equals(isEnded)) {
                finish();
            }
        });
        vm.getRemoteMediaStream().observe(this, adapter::addMediaStream);
        vm.getRejectedUserName().observe(this, /* msg */ this::showToast);
        vm.getByeUserName().observe(this, adapter::removeMediaStreamfromId);
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

    private void initCaller(@NonNull final Intent intent) {
        final String callerId = TelManager.getTelNumber(getApplicationContext());
        final String calleeId = intent.getStringExtra(EXTRA_CALLEE_ID);

        vm.createRoom(callerId);
        vm.dial(calleeId);
    }

    private void initCallee(@NonNull final Intent intent) {
        final String callerId = intent.getStringExtra(EXTRA_CALLER_ID);
        vm.accept(callerId);
    }

    private void showToast(@Nullable final String msg) {
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    public static Intent getCallerLaunchIntent(@NonNull final Context context,
                                               @NonNull final String calleeId) {
        return getLaunchIntent(context, CallActivity.class)
                .putExtra(EXTRA_CALLEE_ID, calleeId)
                .putExtra(EXTRA_IS_CALLER, true);
    }

    @NonNull
    public static Intent getCalleeLaunchIntent(@NonNull final Context context,
                                               @NonNull final String callerId) {
        return getLaunchIntent(context, CallActivity.class)
                .putExtra(EXTRA_CALLER_ID, callerId)
                .putExtra(EXTRA_IS_CALLER, false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
    }
}
