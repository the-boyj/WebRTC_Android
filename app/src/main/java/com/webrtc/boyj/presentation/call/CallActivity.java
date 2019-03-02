package com.webrtc.boyj.presentation.call;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

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

        // Todo : Handle each "Caller" and "Callee"
        final TextView nameView = findViewById(R.id.tv_callee_name);
        final User user = (User) getIntent().getSerializableExtra(EXTRA_OTHER_USER);
        if (user != null) {
            nameView.setText(user.getName());
        }
    }

    @NonNull
    public static Intent getCallerLaunchIntent(@NonNull final Context context,
                                               @NonNull final User user,
                                               @NonNull final User otherUser) {
        final Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_OTHER_USER, otherUser);
        intent.putExtra(EXTRA_IS_CALLER, true);
        return intent;
    }

    @NonNull
    public static Intent getCalleeLaunchIntent(@NonNull final Context context,
                                               @NonNull final User otherUser) {

        final Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(EXTRA_OTHER_USER, otherUser);
        intent.putExtra(EXTRA_IS_CALLER, false);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
    }
}
