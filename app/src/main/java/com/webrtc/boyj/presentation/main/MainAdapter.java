package com.webrtc.boyj.presentation.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.model.User;

public class MainAdapter extends ListAdapter<User, MainViewHolder> {
    interface OnDialListener {
        void onDial(@NonNull final User user);
    }

    MainAdapter() {
        super(DIFF_CALLBACK);
    }

    private OnDialListener onDialListener;

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int i) {
        final User user = getItem(holder.getAdapterPosition());
        holder.bindTo(user);

        holder.getBinding().fabCall.setOnClickListener(v -> {
            if (onDialListener != null) {
                onDialListener.onDial(user);
            }
        });
    }

    void setOnDialListener(OnDialListener onDialListener) {
        this.onDialListener = onDialListener;
    }

    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<User>() {
                @Override
                public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                    return oldItem.equals(newItem);
                }
            };
}
