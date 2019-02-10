package com.webrtc.boyj.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.BR;
import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivityCallBinding;
import com.webrtc.boyj.model.dto.User;
import com.webrtc.boyj.utils.Constants;
import com.webrtc.boyj.viewmodel.CallViewModel;

public class CallActivity extends BaseActivity<ActivityCallBinding, CallViewModel> {
    private static final String KEY_IS_CALLER = "isCaller";
    private static final String KEY_ROOM = "room";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final User user = (User) getIntent().getSerializableExtra(Constants.EXTRA_USER);

        if(user != null) {
            binding.setVariable(BR.item, user);
        }

        // Activity 종료 플래그
        model.getIsActive().observe(this, isActive -> finish());

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
    }

    @Override
    protected CallViewModel getViewModel() {
        return ViewModelProviders.of(this).get(CallViewModel.class);
    }

    public static void goToCallActivity(@NonNull Context context, @NonNull boolean isCaller, @NonNull String room) {
        final Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(KEY_IS_CALLER, isCaller);
        intent.putExtra(KEY_ROOM, room);
        context.startActivity(intent);
    }
}
