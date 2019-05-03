package com.webrtc.boyj.presentation;

import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity {
    protected B binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, getLayoutId());
        binding.setLifecycleOwner(this);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    public static Intent getLaunchIntent(@NonNull final Context context,
                                         @NonNull final Class<?> cls) {
        return new Intent(context, cls);
    }
}
