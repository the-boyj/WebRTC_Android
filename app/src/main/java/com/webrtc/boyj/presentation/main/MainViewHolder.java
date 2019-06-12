package com.webrtc.boyj.presentation.main;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.databinding.ItemUserBinding;

class MainViewHolder extends RecyclerView.ViewHolder {
    private ItemUserBinding binding;

    MainViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    void bindTo(@NonNull User user) {
        binding.setUser(user);
    }

    ItemUserBinding getBinding() {
        return binding;
    }
}
