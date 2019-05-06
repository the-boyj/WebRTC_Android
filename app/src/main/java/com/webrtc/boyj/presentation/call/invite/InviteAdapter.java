package com.webrtc.boyj.presentation.call.invite;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.model.User;

public class InviteAdapter extends ListAdapter<User, InviteViewHolder> {
    public interface OnDialListener {
        void onDial(@NonNull final User user);
    }

    public InviteAdapter() {
        super(DIFF_CALLBACK);
    }

    private OnDialListener onDialListener;

    @NonNull
    @Override
    public InviteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new InviteViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_invite_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InviteViewHolder holder, int i) {
        final User user = getItem(holder.getAdapterPosition());
        holder.bindTo(user);
        holder.getBinding().ivUserPhoto.setOnClickListener(__ -> onDialListener.onDial(user));
    }

    public void setOnDialListener(OnDialListener onDialListener) {
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
