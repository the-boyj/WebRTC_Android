package com.webrtc.boyj.presentation.ringing;

import android.annotation.TargetApi;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.ActivityRingingBinding;
import com.webrtc.boyj.presentation.BaseActivity;
import com.webrtc.boyj.presentation.call.CallActivity;
import com.webrtc.boyj.utils.TelManager;

public class RingingActivity extends BaseActivity<ActivityRingingBinding> {
    private static final String EXTRA_ROOM = "room";
    private static final String EXTRA_CALLER_ID = "callerId";

    private Ringtone ringTone = null;
    private Vibrator vibrator = null;
    private String callerId;

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        turnOnRingTone();

        final String room = getIntent().getStringExtra(EXTRA_ROOM);
        callerId = getIntent().getStringExtra(EXTRA_CALLER_ID);
        final String calleeId = TelManager.getTelNumber(getApplicationContext());

        initViewModel();
        initViews();

        binding.getVm().awaken(room, callerId, calleeId);
    }

    private void initViews() {
        findViewById(R.id.fab_accept).setOnClickListener(__ -> {
            turnOffRingTone();
            startCallActivity();

        });
        findViewById(R.id.fab_reject).setOnClickListener(__ -> {
            turnOffRingTone();
            binding.getVm().reject(callerId);
            finish();
        });
    }

    private void turnOnRingTone() {
        AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if (am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringTone = RingtoneManager.getRingtone(getApplicationContext(), uri);
            ringTone.play();
        } else if (am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
            vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(new long[]{500, 1000}, 1));
            } else {
                vibrator.vibrate(new long[]{500, 1000}, 1);
            }
        }
    }

    private void turnOffRingTone() {
        if (ringTone != null) {
            ringTone.stop();
            ringTone = null;
        }

        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
    }

    private void initViewModel() {
        final RingingViewModel vm = ViewModelProviders.of(this).get(RingingViewModel.class);
        binding.setVm(vm);
    }

    private void startCallActivity() {
        startActivity(CallActivity.getCalleeLaunchIntent(this, callerId));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
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
        super.onDestroy();
        turnOffRingTone();
    }
}
