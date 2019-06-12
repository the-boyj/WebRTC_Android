package com.webrtc.boyj.presentation.common.binding;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.webrtc.boyj.R;

public class FloatingActionButtonBinding {
    @BindingAdapter({"speakerphoneImage"})
    public static void setSpeakerphoneImage(@NonNull final FloatingActionButton fab,
                                            final boolean isSpeakerphone) {
        if (isSpeakerphone) {
            fab.setImageResource(R.drawable.ic_speaker_phone_enabled);
        } else {
            fab.setImageResource(R.drawable.ic_speaker_phone_disabled);
        }
    }
}
