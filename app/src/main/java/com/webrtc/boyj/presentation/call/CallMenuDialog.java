package com.webrtc.boyj.presentation.call;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.webrtc.boyj.R;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.local.room.AppDatabase;
import com.webrtc.boyj.data.source.local.room.dao.UserDao;
import com.webrtc.boyj.databinding.DialogCallMenuBinding;
import com.webrtc.boyj.presentation.call.invite.InviteAdapter;
import com.webrtc.boyj.presentation.call.invite.InviteViewModel;

import java.util.ArrayList;
import java.util.List;

public class CallMenuDialog extends BottomSheetDialogFragment {
    private static final String ARG_USER_LIST = "ARG_USER_LIST";

    private DialogCallMenuBinding binding;

    private List<String> ids;
    private InviteViewModel viewModel;

    public interface OnInviteListener {
        void onInvite(@NonNull final User user);
    }

    private OnInviteListener onInviteListener;

    public void setOnInviteListener(OnInviteListener onInviteListener) {
        this.onInviteListener = onInviteListener;
    }

    public static CallMenuDialog newInstance(@NonNull final List<String> ids) {
        final CallMenuDialog dialog = new CallMenuDialog();
        final Bundle args = new Bundle();
        args.putStringArrayList(ARG_USER_LIST, (ArrayList<String>) ids);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_call_menu, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            ids = getArguments().getStringArrayList(ARG_USER_LIST);
        }
        initViewModel();
        initView();
        viewModel.loadOtherUserList(ids);
    }

    private void initViewModel() {
        final UserDao userDao = AppDatabase.getInstance(getContext()).userDao();
        final InviteViewModel.Factory factory = new InviteViewModel.Factory(userDao);
        viewModel = ViewModelProviders.of(this, factory).get(InviteViewModel.class);
        binding.setInviteViewModel(viewModel);
    }

    private void initView() {
        setCancelable(false);
        initRecyclerView();
        initListener();
    }

    private void initRecyclerView() {
        binding.rvInviteUser.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        final InviteAdapter adapter = new InviteAdapter();
        adapter.setOnDialListener(user -> onInviteListener.onInvite(user));
        binding.rvInviteUser.setAdapter(adapter);
    }

    private void initListener() {
        binding.btnClose.setOnClickListener(__ -> dismiss());
    }
}
