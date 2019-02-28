package com.webrtc.boyj.presentation.call;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.model.User;

public class CallActivity extends AppCompatActivity {
    private static final String EXTRA_USER = "EXTRA_USER";
    private static final String EXTRA_CALLER = "EXTRA_CALLER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
    }

    public static Intent getLaunchIntent(@NonNull final Context context,
                                         @NonNull final User user,
                                         final boolean isCaller) {
        final Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_CALLER, isCaller);
        return intent;
    }
}
