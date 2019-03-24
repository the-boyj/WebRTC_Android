package com.webrtc.boyj.extension.custom;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class SplitLayout extends LinearLayout {
    @Nullable
    private Adapter adapter;
    @Nullable
    private DataSetObserver observer;
    private int column = 1;

    public SplitLayout(Context context) {
        super(context);
        init();
    }

    public SplitLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    private LinearLayout createContainer() {
        final LinearLayout layout = new LinearLayout(getContext());
        final LayoutParams params = new LayoutParams(MATCH_PARENT, 0, 1);
        layout.setOrientation(HORIZONTAL);
        addView(layout, params);
        return layout;
    }

    private void notifyDataSetChanged() {
        if (adapter != null) {
            final int size = adapter.getCount();
            column = (int) Math.round(Math.sqrt(size));
            removeAllViewsInLayout();
            for (int i = 0; i < size; i++) {
                final View view = adapter.onCreateView(this);
                final ViewGroup group = getViewGroup(i);

                final LayoutParams params = (LayoutParams) view.getLayoutParams();
                params.weight = 1;

                group.addView(view, params);
                adapter.onBindView(view, i);
            }
        } else {
            throw new IllegalStateException("Adapter is not exist");
        }
    }

    private ViewGroup getViewGroup(final int position) {
        if (position % column == 0) {
            return createContainer();
        } else {
            return (ViewGroup) getChildAt(position / column);
        }
    }

    public void setAdapter(@NonNull Adapter adapter) {
        if (this.adapter != null && observer != null) {
            this.adapter.unRegisterDataSetObserver(observer);
        } else {
            observer = new AdapterDataObservable();
            adapter.registerDataSetObserver(observer);
            this.adapter = adapter;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (adapter != null && observer != null) {
            adapter.unRegisterDataSetObserver(observer);
        }
    }

    private final class AdapterDataObservable extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }
    }

    public abstract static class Adapter {
        private DataSetObservable dataSetObservable = new DataSetObservable();

        protected abstract View onCreateView(@NonNull final ViewGroup viewGroup);

        protected abstract void onBindView(@NonNull final View view, int position);

        protected abstract int getCount();

        protected void notifyDataSetChanged() {
            dataSetObservable.notifyChanged();
        }

        private void registerDataSetObserver(DataSetObserver observer) {
            dataSetObservable.registerObserver(observer);
        }

        private void unRegisterDataSetObserver(DataSetObserver observer) {
            dataSetObservable.unregisterObserver(observer);
        }
    }
}
