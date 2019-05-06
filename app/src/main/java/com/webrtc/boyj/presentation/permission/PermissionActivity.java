package com.webrtc.boyj.presentation.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jakewharton.rxbinding3.view.RxView;
import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivityPermissionBinding;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.presentation.sign.SignActivity;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import io.reactivex.disposables.CompositeDisposable;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PermissionActivity extends BaseActivity<ActivityPermissionBinding> {
    private static final int PERMISSION_REQUEST_CODE = 100;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (areAllPermissionsGrantedAlready(RECORD_AUDIO, CAMERA, CALL_PHONE)) {
            startSignActivity();
        } else {
            initViews();
        }
    }

    private boolean areAllPermissionsGrantedAlready(final String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void startSignActivity() {
        startActivity(SignActivity.getLaunchIntent(this, SignActivity.class));
        finish();
    }

    private void initViews() {
        addLink();
        initButton();
    }

    private void addLink() {
        final String scheme = getString(R.string.git_hub_url);
        Linkify.addLinks(binding.tvNotice,
                Pattern.compile("이곳"),
                scheme,
                null,
                (match, url) -> "");
    }

    private void initButton() {
        disposables.add(RxView.clicks(binding.btnAccept)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(__ -> requestPermissions()));

        binding.btnDeny.setOnClickListener(__ -> finish());
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{RECORD_AUDIO, CAMERA, CALL_PHONE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (areAllPermissionsGranted(grantResults)) {
                startSignActivity();
            } else {
                Toast.makeText(this, "권한을 확인바랍니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean areAllPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result == PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @NonNull
    public static Intent getLaunchIntent(@NonNull final Context context) {
        return getLaunchIntent(context, PermissionActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_permission;
    }

    @Override
    protected void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }
}
