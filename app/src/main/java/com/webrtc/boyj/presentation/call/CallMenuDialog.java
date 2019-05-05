package com.webrtc.boyj.presentation.call;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.webrtc.boyj.App;
import com.webrtc.boyj.R;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserRepositoryImpl;
import com.webrtc.boyj.data.source.local.preferences.TokenLocalDataSource;
import com.webrtc.boyj.data.source.local.room.AppDatabase;
import com.webrtc.boyj.data.source.local.room.UserLocalDataSource;
import com.webrtc.boyj.data.source.remote.BoyjApiClient;
import com.webrtc.boyj.data.source.remote.UserRemoteDataSource;
import com.webrtc.boyj.databinding.DialogCallMenuBinding;
import com.webrtc.boyj.presentation.call.invite.InviteAdapter;
import com.webrtc.boyj.presentation.call.invite.InviteViewModel;

import java.util.ArrayList;
import java.util.List;

public class CallMenuDialog extends BottomSheetDialogFragment {
    private static final String ARG_USER_LIST = "ARG_USER_LIST";

    private DialogCallMenuBinding binding;
    private OnInviteListener onInviteListener;

    private List<String> ids;

    public interface OnInviteListener {
        void onInvite(@NonNull final User user);
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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            ids = getArguments().getStringArrayList(ARG_USER_LIST);
        }
        initViewModel();
        initView();
    }

    private void initViewModel() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        final InviteViewModel.Factory factory = new InviteViewModel.Factory(
                UserRepositoryImpl.getInstance(
                        UserLocalDataSource.getInstance(AppDatabase.getInstance(App.getContext()).userDao()),
                        UserRemoteDataSource.getInstance(BoyjApiClient.getInstance()),
                        TokenLocalDataSource.getInstance(pref)));
        final InviteViewModel vm = ViewModelProviders.of(this, factory).get(InviteViewModel.class);
        binding.setInviteViewModel(vm);
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
        binding.getInviteViewModel().getOtherUserList().observe(this, adapter::submitList);
        binding.rvInviteUser.setAdapter(adapter);
    }

    private void initListener() {
        binding.btnClose.setOnClickListener(__ -> dismiss());
        binding.fabInvite.setOnClickListener(__ ->
                binding.getInviteViewModel().loadOtherUserList(ids));
    }

    public void setOnInviteListener(OnInviteListener onInviteListener) {
        this.onInviteListener = onInviteListener;
    }
}
