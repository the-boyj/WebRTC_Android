package com.webrtc.boyj.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;

import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivityMainBinding;
import com.webrtc.boyj.model.dto.User;
import com.webrtc.boyj.view.adapter.MainAdapter;
import com.webrtc.boyj.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {
    MainAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new MainAdapter();
        binding.recycler.setAdapter(adapter);
        binding.recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Fab 클릭시 액티비티 이동
        adapter.setOnFabClickListener(this::moveToCallActivity);

        // User 추가시 데이터 반영
        model.getUsers().observe(this, adapter::updateItems);
    }

    private void moveToCallActivity(User user) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainViewModel getViewModel() {
        return ViewModelProviders.of(this).get(MainViewModel.class);
    }
}
