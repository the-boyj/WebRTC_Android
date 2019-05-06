package com.webrtc.boyj.presentation.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.webrtc.boyj.R;


public class NameDialog extends Dialog {
    private static final String ERROR_NAME_LENGTH = "이름을 다시 확인 해 주세요.";

    public interface PositiveButton {
        void onPositive(@NonNull final String name);
    }

    public interface NegativeButton {
        void onNegative();
    }

    private PositiveButton positiveButton;
    private NegativeButton negativeButton;

    public NameDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_name);
        setCancelable(false);

        initView();
    }

    private void initView() {
        findViewById(R.id.tv_positive).setOnClickListener(v -> {
            final TextInputEditText editText = findViewById(R.id.et_name);
            final Editable editable = editText.getText();
            assert editable != null;

            final int maxLength = getContext().getResources().getInteger(R.integer.name_max_length);
            final String text = editable.toString();
            if (!TextUtils.isEmpty(text) && text.length() < maxLength) {
                if (positiveButton != null) {
                    positiveButton.onPositive(text);
                }
                dismiss();
            } else {
                Toast.makeText(getContext().getApplicationContext(), ERROR_NAME_LENGTH, Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.tv_negative).setOnClickListener(v -> {
            if (negativeButton != null) {
                negativeButton.onNegative();
            }
            dismiss();
        });
    }

    public void setPositiveButton(PositiveButton positiveButton) {
        this.positiveButton = positiveButton;
    }

    public void setNegativeButton(NegativeButton negativeButton) {
        this.negativeButton = negativeButton;
    }
}
