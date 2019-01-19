package com.webrtc.boyj.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;

import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivityMainBinding;
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

        // 아이템 클릭 리스너 필요한 경우
        // adapter.setOnItemClickListener(System.out::println);

        model.getUsers().observe(this, adapter::updateItems);
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
