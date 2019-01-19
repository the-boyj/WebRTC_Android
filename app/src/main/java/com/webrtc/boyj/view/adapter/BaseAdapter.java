package com.webrtc.boyj.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {
    List<T> itemList;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        if(itemList == null) {
            return 0;
        }

        return this.itemList.size();
    }

    /**
     * itemList 전체 수정 */
    public void updateItems(List<T> items) {
        if(itemList == null) {
            itemList = new ArrayList<>();
        }
        this.itemList.clear();
        this.itemList.addAll(items);

        notifyDataSetChanged();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(view -> {
            // Item click listener 등록
            if(onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });

        onBindView((H) holder, position);
    }

    protected abstract void onBindView(H holder, int position);
}
