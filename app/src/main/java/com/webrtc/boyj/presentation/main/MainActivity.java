package com.webrtc.boyj.presentation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.common.IDManager;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.databinding.ActivityMainBinding;
import com.webrtc.boyj.di.Injection;
import com.webrtc.boyj.presentation.call.CallActivity;
import com.webrtc.boyj.presentation.common.activity.BaseActivity;
import com.webrtc.boyj.presentation.settings.SettingsActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private String id;


    @Override
    protected void onActivityCreated(@Nullable Bundle savedInstanceState) {
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
        setSupportActionBar(binding.layoutToolbar.toolbar);
        final ActionBar toolbar = getSupportActionBar();
        if (toolbar != null) {
            toolbar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initViewModel() {
        final MainViewModel vm = ViewModelProviders.of(this,
                Injection.providerMainViewModelFactory(this)).get(MainViewModel.class);
        vm.loadProfile(id);
        vm.loadOtherUserList(id);
        binding.setVm(vm);
    }

    private void initRecyclerView() {
        final MainAdapter adapter = new MainAdapter();
        adapter.setOnDialListener(this::startCallActivity);

        final SwipeRefreshLayout layout = findViewById(R.id.swipe_refresh_layout);
        layout.setOnRefreshListener(() -> {
            binding.getVm().loadNewUserList(id);
            layout.setRefreshing(false);
        });
        binding.rvUser.setAdapter(adapter);
    }

    private void startCallActivity(@NonNull final User user) {
        startActivity(CallActivity.getCallerLaunchIntent(this, user.getId()));
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
