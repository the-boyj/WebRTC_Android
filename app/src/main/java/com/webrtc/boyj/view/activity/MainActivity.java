package com.webrtc.boyj.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivityMainBinding;
import com.webrtc.boyj.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainViewModel getViewModel() {
        return ViewModelProviders.of(this).get(MainViewModel.class);
    }
}
