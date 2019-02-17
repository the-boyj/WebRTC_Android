package com.webrtc.boyj.presentation.main;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.webrtc.boyj.R;
import com.webrtc.boyj.data.repository.UserRepositoryImpl;
import com.webrtc.boyj.databinding.ActivityMainBinding;
import com.webrtc.boyj.presentation.BaseActivity;

import java.util.List;

import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private void checkPermission() {
        TedPermission.with(this)
                .setPermissions(READ_PHONE_STATE)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        final TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                        final String tel = tm.getLine1Number().replace("+82", "0");
                        init(tel);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(getApplicationContext(), "권한 없음", Toast.LENGTH_SHORT).show();
                    }
                }).check();
    }

    private void init(@NonNull final String tel) {
        initViewModel(tel);
        initRecyclerView();
        subscribeViewModel();
        binding.getViewModel().init();
    }

    private void initViewModel(@NonNull final String tel) {
        final MainViewModel vm = ViewModelProviders.of(this,
                new MainViewModelFactory(UserRepositoryImpl.getInstance(
                        FirebaseFirestore.getInstance(),
                        PreferenceManager.getDefaultSharedPreferences(this)), tel))
                .get(MainViewModel.class);
        binding.setViewModel(vm);
    }

    private void initRecyclerView() {
        final MainAdapter adapter = new MainAdapter();
        binding.rvUser.setAdapter(adapter);
        binding.getViewModel().getUserList().observe(this, adapter::submitList);
    }

    private void subscribeViewModel() {
        binding.getViewModel().getMyUser().observe(this, user -> {
            Log.d("Melon", user.getName());
            Log.d("Melon", user.getTel());
            Log.d("Melon", user.getDeviceToken());
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
