package com.webrtc.boyj.presentation.splash;

import android.os.Bundle;

import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivitySplashBinding;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.presentation.permission.PermissionActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    private static final int SPLASH_TIME = 2;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disposable = Single.timer(SPLASH_TIME, TimeUnit.SECONDS).
                subscribe(__ -> {
                    startPermissionActivity();
                    disposable.dispose();
                });
    }

    private void startPermissionActivity() {
        startActivity(PermissionActivity.getLaunchIntent(this));
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }
}
