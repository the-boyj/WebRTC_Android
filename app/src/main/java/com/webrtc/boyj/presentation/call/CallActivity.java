package com.webrtc.boyj.presentation.call;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.databinding.ActivityCallBinding;
import com.webrtc.boyj.presentation.BaseActivity;

public class CallActivity extends BaseActivity<ActivityCallBinding> {
    private static final String EXTRA_USER = "EXTRA_USER";
    private static final String EXTRA_CALLER = "EXTRA_CALLER";

    @NonNull
    public static Intent getLaunchIntent(@NonNull final Context context,
                                         @NonNull final User user,
                                         final boolean isCaller) {
        final Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_CALLER, isCaller);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
    }
}
