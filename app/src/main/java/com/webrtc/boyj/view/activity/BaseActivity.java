package com.webrtc.boyj.view.activity;

import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.webrtc.boyj.BR;
import com.webrtc.boyj.viewmodel.BaseViewModel;

public abstract class BaseActivity<B extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity {
    protected B binding;
    protected V model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 화면 세로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = DataBindingUtil.setContentView(this, getLayoutId());
        model = getViewModel();
        binding.setVariable(BR.model, model);

        model.onCreate();
    }

    protected abstract int getLayoutId();

    protected abstract V getViewModel();
}
