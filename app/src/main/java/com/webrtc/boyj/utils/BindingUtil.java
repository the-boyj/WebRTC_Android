package com.webrtc.boyj.utils;

import android.databinding.BindingConversion;
import android.view.View;

public class BindingUtil {

    @BindingConversion
    public static int setVisiblity(boolean isVisible) {
        return isVisible ? View.VISIBLE : View.GONE;
    }
}
