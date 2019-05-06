package com.webrtc.boyj.presentation.common.binding;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class TextBinding {
    @SuppressLint("DefaultLocale")
    @BindingAdapter({"callTime"})
    public static void setCallTime(@NonNull final TextView textView, final int time) {
        if (time > 0) {
            int min = time / 60;
            int sec = time % 60;
            textView.setVisibility(View.VISIBLE);
            textView.setText(String.format("%02d:%02d", min, sec));
        }
    }
}
