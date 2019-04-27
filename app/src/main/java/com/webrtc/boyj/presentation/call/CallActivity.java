package com.webrtc.boyj.presentation.call;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.webrtc.boyj.R;
import com.webrtc.boyj.api.boyjrtc.BoyjRTC;
import com.webrtc.boyj.databinding.ActivityCallBinding;
import com.webrtc.boyj.extension.custom.SplitLayout;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.utils.App;
import com.webrtc.boyj.utils.TelManager;

public class CallActivity extends BaseActivity<ActivityCallBinding> {
    private static final String EXTRA_CALLER_ID = "EXTRA_CALLER_ID";
    private static final String EXTRA_CALLEE_ID = "EXTRA_CALLEE_ID";
    private static final String EXTRA_IS_CALLER = "EXTRA_IS_CALLER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        final boolean isCaller = intent.getBooleanExtra(EXTRA_IS_CALLER, true);

        initViews();
        initViewModel();

        if (isCaller) {
            initCaller(intent);
        } else {
            initCallee(intent);
        }
    }

    private void initViews() {
        initFloatingButton();
        initSplitLayout();
    }

    private void initFloatingButton() {
        findViewById(R.id.iv_call_menu).setOnClickListener(__ -> showCallMenuDialog());
    }

    private void showCallMenuDialog() {
        CallMenuDialog.newInstance()
                .show(getSupportFragmentManager(), "CallMenuDialog");
    }

    private void initSplitLayout() {
        final CallAdapter adapter = new CallAdapter();
        final SplitLayout splitLayout = findViewById(R.id.splitLayout);
        splitLayout.setAdapter(adapter);
    }

    private void initViewModel() {
        final BoyjRTC boyjRTC = generateBoyjRTC();
        final CallViewModelFactory factory = new CallViewModelFactory(boyjRTC);
        final CallViewModel viewModel =
                ViewModelProviders.of(this, factory).get(CallViewModel.class);
        binding.setVm(viewModel);
        subscribeViewModel();
    }

    private BoyjRTC generateBoyjRTC() {
        final BoyjRTC boyjRTC = new BoyjRTC();
        boyjRTC.initRTC(App.getContext());
        return boyjRTC;
    }

    private void subscribeViewModel() {
        subscribeRejected();
        subscribeLeaved();
        subscribeEndOfCall();
    }

    private void subscribeRejected() {
        binding.getVm().getRejectedUserName().observe(this, userName ->
                showToast(userName + "가 통화를 거절하였습니다."));
    }

    private void subscribeLeaved() {
        binding.getVm().getLeavedUserName().observe(this, userName ->
                showToast(userName + "가 통화를 종료하였습니다."));
    }

    private void subscribeEndOfCall() {
        binding.getVm().getEndOfCall().observe(this, isEnded -> {
            if (Boolean.TRUE.equals(isEnded)) {
                finish();
            }
        });
    }

    private void initCaller(@NonNull final Intent intent) {
        final String callerId = TelManager.getTelNumber(getApplicationContext());
        final String calleeId = intent.getStringExtra(EXTRA_CALLEE_ID);
        binding.getVm().initCaller(callerId, calleeId);
    }

    private void initCallee(@NonNull final Intent intent) {
        final String callerId = intent.getStringExtra(EXTRA_CALLER_ID);
        binding.getVm().initCallee(callerId);
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
