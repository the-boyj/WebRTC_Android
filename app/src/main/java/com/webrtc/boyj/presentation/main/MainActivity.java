package com.webrtc.boyj.presentation.main;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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
    @Nullable
    private String tel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        checkPermission();
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        final ActionBar toolbar = getSupportActionBar();
        if (toolbar != null) {
            toolbar.setDisplayShowTitleEnabled(false);
        }

    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private void checkPermission() {
        TedPermission.with(this)
                .setPermissions(READ_PHONE_STATE)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        final TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                        final String number = tm.getLine1Number();
                        if (TextUtils.isEmpty(number)) {
                            notExistPhoneNumber();
                        } else {
                            tel = number.replace("+82", "0");
                            init();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        showToast(getString(R.string.ERROR_DENIED_PERMISSION));
                    }
                }).check();
    }

    private void init() {
        assert tel != null;

        initViewModel();
        initRecyclerView();
        subscribeViewModel();
        binding.getVm().init(tel);
    }

    private void initViewModel() {
        assert tel != null;

        final MainViewModel vm = ViewModelProviders.of(this,
                new MainViewModelFactory(UserRepositoryImpl.getInstance(
                        FirebaseFirestore.getInstance(),
                        PreferenceManager.getDefaultSharedPreferences(this))))
                .get(MainViewModel.class);

        binding.setVm(vm);
    }

    private void initRecyclerView() {
        final MainAdapter adapter = new MainAdapter();
        binding.rvUser.setAdapter(adapter);
    }

    private void subscribeViewModel() {
        binding.getVm().getError().observe(this,
                e -> showToast(getString(R.string.ERROR_DEFAULT)));
    }

    private void notExistPhoneNumber() {
        showToast(getString(R.string.ERROR_PHONE_NUMBER_NOT_EXIST));
    }

    private void showToast(@NonNull final String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_update_profile) {
            // Todo : 이름 변경
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
