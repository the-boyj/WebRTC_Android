package com.webrtc.boyj.presentation.main;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.common.IDManager;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserRepositoryImpl;
import com.webrtc.boyj.data.source.local.preferences.TokenLocalDataSource;
import com.webrtc.boyj.data.source.local.room.AppDatabase;
import com.webrtc.boyj.data.source.local.room.UserLocalDataSource;
import com.webrtc.boyj.data.source.remote.BoyjApiClient;
import com.webrtc.boyj.data.source.remote.UserRemoteDataSource;
import com.webrtc.boyj.databinding.ActivityMainBinding;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.presentation.settings.SettingsActivity;
import com.webrtc.boyj.presentation.call.CallActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        id = IDManager.getSavedUserId(this);
        initToolbar();
        initViewModel();
        initRecyclerView();
        subscribeViewModel();
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        final ActionBar toolbar = getSupportActionBar();
        if (toolbar != null) {
            toolbar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initViewModel() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final MainViewModelFactory factory = new MainViewModelFactory(
                UserRepositoryImpl.getInstance(
                        UserLocalDataSource.getInstance(AppDatabase.getInstance(this).userDao()),
                        UserRemoteDataSource.getInstance(BoyjApiClient.getInstance()),
                        TokenLocalDataSource.getInstance(pref)));
        final MainViewModel vm = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        vm.loadProfile(id);
        vm.loadOtherUserList(id);
        binding.setVm(vm);
    }

    private void initRecyclerView() {
        final MainAdapter adapter = new MainAdapter();
        adapter.setOnDialListener(this::startCallActivity);
        binding.rvUser.setAdapter(adapter);
    }

    private void startCallActivity(@NonNull final User user) {
        startActivity(CallActivity.getCallerLaunchIntent(this, user.getId()));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private void subscribeViewModel() {
        binding.getVm().getError().observe(this,
                e -> {
                    showToast(getString(R.string.ERROR_DEFAULT));
                    e.printStackTrace();
                });
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
            showDialog();
        } else if (item.getItemId() == R.id.menu_refresh_user_list) {
            binding.getVm().loadNewUserList(id);
        } else if (item.getItemId() == R.id.menu_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        final NameDialog dialog = new NameDialog(this);
        dialog.setPositiveButton(name -> binding.getVm().updateUserName(id, name));
        dialog.show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
