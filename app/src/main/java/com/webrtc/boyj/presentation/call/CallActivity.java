package com.webrtc.boyj.presentation.call;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.common.IDManager;
import com.webrtc.boyj.databinding.ActivityCallBinding;
import com.webrtc.boyj.di.Injection;
import com.webrtc.boyj.presentation.common.activity.BaseActivity;
import com.webrtc.boyj.presentation.common.view.SplitLayout;

import java.util.List;

public class CallActivity extends BaseActivity<ActivityCallBinding> {
    private static final String EXTRA_CALLEE_ID = "EXTRA_CALLEE_ID";

    private CallAdapter adapter;

    private String id;

    private CallViewModel callViewModel;

    @Override
    protected void onActivityCreated(@Nullable Bundle savedInstanceState) {
        final String calleeId = getIntent().getStringExtra(EXTRA_CALLEE_ID);
        id = IDManager.getSavedUserId(this);

        initViews();
        initViewModel();
        initCamera();
        initCall(calleeId);
    }

    private void initViews() {
        initSplitLayout();
        initInviteFab();
    }

    private void initSplitLayout() {
        adapter = new CallAdapter();
        final SplitLayout splitLayout = findViewById(R.id.splitLayout);
        splitLayout.setAdapter(adapter);
    }

    private void initInviteFab() {
        findViewById(R.id.fab_right).setOnClickListener(__ -> {
            final List<String> ids = adapter.getUserListInRoomIncludingMe(id);
            final CallMenuDialog dialog = CallMenuDialog.newInstance(ids);
            dialog.setOnInviteListener(user -> {
                showToast(user.getName() + " 에게 통화를 요청하였습니다.");
                callViewModel.invite(user.getId());
                dialog.dismiss();
            });
            dialog.show(getSupportFragmentManager(), "CallMenuDialog");
        });
    }

    private void initViewModel() {
        initCallViewModel();
        initSpeakerViewModel();
    }

    private void initCallViewModel() {
        callViewModel = ViewModelProviders.of(this,
                Injection.providerCallViewModelFactory()).get(CallViewModel.class);
        binding.setVm(callViewModel);
        subscribeViewModel();
    }

    private void subscribeViewModel() {
        callViewModel.getRejectedUserName().observe(this, userName ->
                showToast(userName + "가 통화를 거절하였습니다."));

        callViewModel.getEndOfCall().observe(this, isEnded -> {
            if (Boolean.TRUE.equals(isEnded)) {
                finish();
            }
        });

        callViewModel.getError().observe(this, e ->
                showToast(getString(R.string.ERROR_DEFAULT)));
    }

    private void initSpeakerViewModel() {
        final SpeakerViewModel speakerViewModel =
                ViewModelProviders.of(
                        this,
                        Injection.providerSpeakerViewModelFactory(this)
                ).get(SpeakerViewModel.class);
        binding.setSpeakerViewModel(speakerViewModel);
    }

    private void initCamera() {
        callViewModel.initLocalStream();
    }

    private void initCall(@Nullable final String calleeId) {
        if (calleeId != null) {
            initCaller(id, calleeId);
        } else {
            initCallee();
        }
    }

    private void initCaller(@NonNull final String callerId,
                            @NonNull final String calleeId) {
        callViewModel.initCaller(callerId, calleeId);
    }

    private void initCallee() {
        callViewModel.initCallee();
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
                .putExtra(EXTRA_CALLEE_ID, calleeId);
    }

    @NonNull
    public static Intent getCalleeLaunchIntent(@NonNull final Context context) {
        return getLaunchIntent(context, CallActivity.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
    }

    @Override
    public void onBackPressed() {
        // disabled back press
    }
}
