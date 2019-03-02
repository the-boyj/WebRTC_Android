package com.webrtc.boyj.presentation.callon;

import android.os.Bundle;

import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivityCallOnBinding;
import com.webrtc.boyj.presentation.BaseActivity;

public class CallOnActivity extends BaseActivity<ActivityCallOnBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call_on;
    }
}
