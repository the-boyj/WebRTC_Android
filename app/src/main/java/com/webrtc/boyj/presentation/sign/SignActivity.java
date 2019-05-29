package com.webrtc.boyj.presentation.sign;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.common.IDManager;
import com.webrtc.boyj.data.source.TokenDataSource;
import com.webrtc.boyj.data.source.local.preferences.TokenLocalDataSource;
import com.webrtc.boyj.databinding.ActivitySignBinding;
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
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final TokenDataSource tokenDataSource = TokenLocalDataSource.getInstance(pref);
        final SignViewModel.Factory factory = new SignViewModel.Factory(tokenDataSource);

        viewModel = ViewModelProviders.of(this, factory).get(SignViewModel.class);
        binding.setVm(viewModel);
        subscribeViewModel();
    }

    private void subscribeViewModel() {
        viewModel.getSignIn().observe(this, id -> {
            Toast.makeText(this, id + " 님 환영합니다", Toast.LENGTH_SHORT).show();
            viewModel.checkAndSetNewTokenStatus(savedId);
            IDManager.saveUserId(this, id);
            startMainActivity();
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
