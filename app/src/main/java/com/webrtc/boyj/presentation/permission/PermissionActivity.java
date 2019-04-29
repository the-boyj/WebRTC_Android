package com.webrtc.boyj.presentation.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.util.Linkify;
import android.widget.Toast;

import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivityPermissionBinding;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.presentation.sign.SignActivity;

import java.util.regex.Pattern;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PermissionActivity extends BaseActivity<ActivityPermissionBinding> {
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (areAllPermissionsGrantedAlready()) {
            startSignActivity();
        } else {
            initViews();
        }
    }

    private boolean areAllPermissionsGrantedAlready() {
        return isPermissionGranted(RECORD_AUDIO, CAMERA, CALL_PHONE);
    }

    private boolean isPermissionGranted(final String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void startSignActivity() {
        startActivity(SignActivity.getLaunchIntent(this));
        overridePendingTransition(0, 0);
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
        binding.btnAccept.setOnClickListener(__ -> requestPermissions());
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
}
