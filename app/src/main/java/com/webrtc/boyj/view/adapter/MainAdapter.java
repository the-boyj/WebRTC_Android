package com.webrtc.boyj.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webrtc.boyj.BR;
import com.webrtc.boyj.R;
import com.webrtc.boyj.databinding.UserItemBinding;
import com.webrtc.boyj.model.dto.User;

public class MainAdapter extends BaseAdapter<User, MainAdapter.ViewHolder>{
    private OnFabClickListener onFabClickListener;

    public void setOnFabClickListener(OnFabClickListener onFabClickListener) {
        this.onFabClickListener = onFabClickListener;
    }

    @Override
    protected void onBindView(ViewHolder holder, int position) {
        holder.binding.setVariable(BR.item, getItemList().get(position));
        holder.binding.fabCall.setOnClickListener(v -> onFabClickListener.onFabClick(getItemList().get(position)));
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        UserItemBinding binding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
