package com.webrtc.boyj.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivityCallBinding;
import com.webrtc.boyj.viewmodel.CallViewModel;

public class CallActivity extends BaseActivity<ActivityCallBinding, CallViewModel> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
    }

    @Override
    protected CallViewModel getViewModel() {
        return ViewModelProviders.of(this).get(CallViewModel.class);
    }
}
