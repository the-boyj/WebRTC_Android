package com.webrtc.boyj.presentation.ringing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.common.IDManager;
import com.webrtc.boyj.data.source.UserRepository;
import com.webrtc.boyj.data.source.UserRepositoryImpl;
import com.webrtc.boyj.data.source.local.preferences.TokenLocalDataSource;
import com.webrtc.boyj.data.source.local.room.AppDatabase;
import com.webrtc.boyj.data.source.local.room.UserLocalDataSource;
import com.webrtc.boyj.data.source.remote.BoyjApiClient;
import com.webrtc.boyj.data.source.remote.UserRemoteDataSource;
import com.webrtc.boyj.databinding.ActivityRingingBinding;
import com.webrtc.boyj.presentation.call.CallActivity;
import com.webrtc.boyj.presentation.common.activity.BaseActivity;
import com.webrtc.boyj.utils.RingtoneLoader;
import com.webrtc.boyj.utils.WakeManager;

public class RingingActivity extends BaseActivity<ActivityRingingBinding> {
    private static final String EXTRA_ROOM = "room";
    private static final String EXTRA_CALLER_ID = "callerId";

    private String callerId;
    private RingingViewModel viewModel;

    @Override
    protected void onActivityCreated(@Nullable Bundle savedInstanceState) {
        RingtoneLoader.ring(this);
        WakeManager.turnOnScreen(this);

        initViews();
        initViewModel();
        awaken();
    }

    private void initViews() {
        binding.fabAccept.setOnClickListener(__ -> startCallActivity());
        binding.fabReject.setOnClickListener(__ -> {
            viewModel.reject(callerId);
            finish();
        });
    }

    private void initViewModel() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final UserRepository repository = UserRepositoryImpl.getInstance(
                UserLocalDataSource.getInstance(AppDatabase.getInstance(this).userDao()),
                UserRemoteDataSource.getInstance(BoyjApiClient.getInstance()),
                TokenLocalDataSource.getInstance(pref));
        final RingingViewModel.Factory factory = new RingingViewModel.Factory(repository);
        viewModel = ViewModelProviders.of(this, factory).get(RingingViewModel.class);
        binding.setVm(viewModel);

        subscribeViewModel();
    }

    private void subscribeViewModel() {
        viewModel.getError().observe(this, error -> {
            Toast.makeText(this, getString(R.string.ERROR_DEFAULT), Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        });
    }

    private void startCallActivity() {
        startActivity(CallActivity.getCalleeLaunchIntent(this));
        finish();
    }

    private void awaken() {
        final String room = getIntent().getStringExtra(EXTRA_ROOM);
        final String calleeId = IDManager.getSavedUserId(this);
        assert calleeId != null;

        callerId = getIntent().getStringExtra(EXTRA_CALLER_ID);
        viewModel.loadCallerProfile(callerId);
        viewModel.awaken(room, callerId, calleeId);
    }

    public static Intent getLaunchIntent(@NonNull final Context context,
                                         @NonNull final String room,
                                         @NonNull final String callerId) {
        return getLaunchIntent(context, RingingActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(EXTRA_ROOM, room)
                .putExtra(EXTRA_CALLER_ID, callerId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ringing;
    }

    @Override
    public void onBackPressed() {
        // disabled back press
    }

    @Override
    protected void onDestroy() {
        RingtoneLoader.cancelAndRelease();
        super.onDestroy();
    }
}
