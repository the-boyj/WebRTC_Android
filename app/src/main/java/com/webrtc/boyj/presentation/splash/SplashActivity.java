package com.webrtc.boyj.presentation.splash;

import android.os.Bundle;

import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivitySplashBinding;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.presentation.main.MainActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    private static final int SPLASH_TIME = 2;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disposable = Single.timer(SPLASH_TIME, TimeUnit.SECONDS)
                .subscribe(__ -> startMainActivity());
    }

    private void startMainActivity() {
        startActivity(MainActivity.getLaunchIntent(this));
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
