package com.webrtc.boyj.presentation.sign;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.Toast;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.common.IDManager;
import com.webrtc.boyj.databinding.ActivitySignBinding;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.presentation.main.MainActivity;

public class SignActivity extends BaseActivity<ActivitySignBinding> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        loadMyId();
    }

    private void initViewModel() {
        final SignViewModel viewModel = ViewModelProviders.of(this).get(SignViewModel.class);
        binding.setVm(viewModel);
        subscribeViewModel();
    }

    private void subscribeViewModel() {
        binding.getVm().getSignIn().observe(this, id -> {
            if (id != null) {
                Toast.makeText(this, id + " 님 환영합니다", Toast.LENGTH_SHORT).show();
                IDManager.saveUserId(this, id);
                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        startActivity(MainActivity.getLaunchIntent(this, MainActivity.class));
        finish();
    }

    private void loadMyId() {
        binding.getVm().setId(IDManager.getSavedUserId(this));
    }

    @NonNull
    public static Intent getLaunchIntent(@NonNull final Context context) {
        return getLaunchIntent(context, SignActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign;
    }
}
