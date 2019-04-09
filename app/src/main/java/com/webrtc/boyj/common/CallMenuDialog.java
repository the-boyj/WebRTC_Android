package com.webrtc.boyj.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webrtc.boyj.R;

public class CallMenuDialog extends BottomSheetDialogFragment {
    private View closeView;
    private FloatingActionButton inviteButton;
    private RecyclerView inviteRecycler;

    public static CallMenuDialog getInstance() {
        return new CallMenuDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_call_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
    }

    private void initView(View view) {
        setCancelable(false);

        closeView = view.findViewById(R.id.btn_close);
        inviteButton = view.findViewById(R.id.fab_invite);
        inviteRecycler = view.findViewById(R.id.rv_invite_user);
        initRecyclerView();
        initListener();
    }

    private void initRecyclerView() {
        inviteRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        // inviteRecycler.setAdapter();
    }

    private void initListener() {
        closeView.setOnClickListener(v -> dismiss());
        inviteButton.setOnClickListener(v -> {
            inviteRecycler.setVisibility(View.VISIBLE);
        });
    }
}
