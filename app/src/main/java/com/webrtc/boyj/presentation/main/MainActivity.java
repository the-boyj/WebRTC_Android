package com.webrtc.boyj.presentation.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.webrtc.boyj.R;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserRepositoryImpl;
import com.webrtc.boyj.data.source.preferences.TokenLocalDataSource;
import com.webrtc.boyj.data.source.remote.UserRemoteDataSource;
import com.webrtc.boyj.databinding.ActivityMainBinding;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.presentation.call.CallActivity;
import com.webrtc.boyj.utils.TelManager;

import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
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

    private void checkPermission() {
        TedPermission.with(getApplicationContext())
                .setPermissions(READ_PHONE_STATE, RECORD_AUDIO, CAMERA)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        tel = TelManager.getTelNumber(getApplicationContext());
                        init();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        showToast(getString(R.string.ERROR_PERMISSION_DENIED));
                    }
                }).check();
    }

    private void init() {
        initViewModel();
        initRecyclerView();
        subscribeViewModel();
    }

    private void initViewModel() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final MainViewModelFactory factory = new MainViewModelFactory(
                UserRepositoryImpl.getInstance(
                        UserRemoteDataSource.getInstance(),
                        TokenLocalDataSource.getInstance(pref)));
        final MainViewModel vm = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        vm.init(tel);
        binding.setVm(vm);
    }

    private void initRecyclerView() {
        final MainAdapter adapter = new MainAdapter();
        adapter.setOnDialListener(this::startCallActivity);
        binding.rvUser.setAdapter(adapter);
    }

    private void subscribeViewModel() {
        binding.getVm().getError().observe(this,
                e -> showToast(getString(R.string.ERROR_DEFAULT)));
    }

    private void showDialog() {
        final NameDialog dialog = new NameDialog(this);
        dialog.setPositiveButton(name -> binding.getVm().updateUserName(tel, name));
        dialog.show();
    }

    private void showToast(@NonNull final String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startCallActivity(@NonNull final User user) {
        startActivity(CallActivity.getCallerLaunchIntent(this, user.getTel()));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_update_profile) {
            showDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
