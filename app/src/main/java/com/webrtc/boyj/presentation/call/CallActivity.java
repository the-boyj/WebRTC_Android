package com.webrtc.boyj.presentation.call;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.databinding.ActivityCallBinding;
import com.webrtc.boyj.presentation.BaseActivity;

public class CallActivity extends BaseActivity<ActivityCallBinding> {
    private static final String EXTRA_USER = "EXTRA_USER";
    private static final String EXTRA_OTHER_USER = "EXTRA_OTHER_USER";
    private static final String EXTRA_IS_CALLER = "EXTRA_IS_CALLER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();

        if (intent == null) {
            showToast(getString(R.string.ERROR_DEFAULT));
            finish();
        } else {
            final User user = (User) intent.getSerializableExtra(EXTRA_USER);
            final User otherUser = (User) intent.getSerializableExtra(EXTRA_OTHER_USER);
            final boolean isCaller = intent.getBooleanExtra(EXTRA_IS_CALLER, true);

            initViews();
            initViewModel(otherUser);

            // Todo : Handle "Caller" and "Callee" by next issue
            if (isCaller) {

            } else {

            }
        }
    }

    private void initViews() {
        findViewById(R.id.fab_hang_up).setOnClickListener(__ -> hangUp());
    }

    private void initViewModel(@NonNull final User otherUser) {
        final CallViewModel vm = ViewModelProviders.of(this,
                new CallViewModelFactory(otherUser)).get(CallViewModel.class);

        binding.setVm(vm);
    }

    private void showToast(@NonNull final String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // Todo : Hangup handling
    private void hangUp() {
        finish();
    }

    @NonNull
    public static Intent getLaunchIntent(@NonNull final Context context,
                                         @NonNull final User otherUser,
                                         @NonNull final User user,
                                         final boolean isCaller) {
        return getLaunchIntent(context, CallActivity.class)
                .putExtra(EXTRA_OTHER_USER, otherUser)
                .putExtra(EXTRA_USER, user)
                .putExtra(EXTRA_IS_CALLER, isCaller);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
    }
}
