package com.webrtc.boyj.presentation.common.binding;

import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputLayout;

public class EditTextBinding {

    @BindingAdapter({"validId"})
    public static void setHintOrError(TextInputLayout layout, boolean isValid) {
        if (isValid) {
            layout.setHelperText("최초 아이디 등록이 필요합니다");
        } else {
            layout.setError("아이디를 확인해 주세요");
        }
    }
}
