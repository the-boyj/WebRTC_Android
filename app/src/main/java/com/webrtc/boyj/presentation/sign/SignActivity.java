package com.webrtc.boyj.presentation.sign;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.common.IDManager;
import com.webrtc.boyj.databinding.ActivitySignBinding;
import com.webrtc.boyj.di.Injection;
import com.webrtc.boyj.presentation.common.activity.BaseActivity;
import com.webrtc.boyj.presentation.main.MainActivity;

public class SignActivity extends BaseActivity<ActivitySignBinding> {
    private String savedId;
    private SignViewModel viewModel;

    @Override
    protected void onActivityCreated(@Nullable Bundle savedInstanceState) {
        savedId = IDManager.getSavedUserId(this);
        initViewModel();
        viewModel.setIdField(savedId);
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this,
                Injection.providerSignViewModelFactory(this)).get(SignViewModel.class);
        binding.setVm(viewModel);
        subscribeViewModel();
    }

    private void subscribeViewModel() {
        viewModel.getSignInEvent().observe(this, event -> {
            final String content = event.getContentIfNotHandled();
            if (content != null) {
                Toast.makeText(this, content + " 님 환영합니다", Toast.LENGTH_SHORT).show();
                viewModel.checkAndSetNewTokenStatus(savedId);
                IDManager.saveUserId(this, content);
                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        startActivity(MainActivity.getLaunchIntent(this, MainActivity.class));
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign;
    }
}
