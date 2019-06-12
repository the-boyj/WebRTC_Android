package com.webrtc.boyj.presentation.splash;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivitySplashBinding;
import com.webrtc.boyj.presentation.common.activity.BaseActivity;
import com.webrtc.boyj.presentation.permission.PermissionActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    private static final int SPLASH_TIME_SECONDS = 2;
    private Disposable disposable;

    @Override
    protected void onActivityCreated(@Nullable Bundle savedInstanceState) {
        disposable = Single.timer(SPLASH_TIME_SECONDS, TimeUnit.SECONDS).
                subscribe(__ -> startPermissionActivity());
    }

    private void startPermissionActivity() {
        startActivity(PermissionActivity.getLaunchIntent(this));
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onDestroy() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }
}
