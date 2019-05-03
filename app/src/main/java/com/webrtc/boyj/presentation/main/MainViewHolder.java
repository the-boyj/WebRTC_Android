package com.webrtc.boyj.presentation.main;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

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
