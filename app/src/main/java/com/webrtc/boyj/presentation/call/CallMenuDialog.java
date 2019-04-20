package com.webrtc.boyj.presentation.call;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserRepositoryImpl;
import com.webrtc.boyj.data.source.preferences.TokenDataSourceImpl;
import com.webrtc.boyj.data.source.remote.UserRemoteDataSource;
import com.webrtc.boyj.databinding.DialogCallMenuBinding;
import com.webrtc.boyj.presentation.call.invite.InviteAdapter;
import com.webrtc.boyj.presentation.call.invite.InviteViewModel;
import com.webrtc.boyj.utils.App;
import com.webrtc.boyj.utils.TelManager;

public class CallMenuDialog extends BottomSheetDialogFragment {
    private DialogCallMenuBinding binding;
    private OnInviteListener onInviteListener;

    public interface OnInviteListener {
        void onInvite(@NonNull final User user);
    }

    public static CallMenuDialog newInstance() {
        return new CallMenuDialog();
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
        initViewModel();
        initView();

        binding.getInviteViewModel()
                .init(TelManager.getTelNumber(App.getContext()));
    }

    private void initViewModel() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(App.getContext());

        final InviteViewModel.Factory factory = new InviteViewModel.Factory(
                UserRepositoryImpl.getInstance(
                        UserRemoteDataSource.getInstance(),
                        TokenDataSourceImpl.getInstance(pref)));
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

        adapter.setOnDialListener(user ->
                onInviteListener.onInvite(user));

        binding.rvInviteUser.setAdapter(adapter);
        binding.getInviteViewModel()
                .getOtherUserList()
                .observe(this, userList -> {
                    if (userList != null) {
                        adapter.submitList(userList);
                    }
                });
    }

    private void initListener() {
        binding.btnClose.setOnClickListener(__ -> dismiss());
        binding.fabInvite.setOnClickListener(__ -> binding.rvInviteUser.setVisibility(View.VISIBLE));
    }

    public void setOnInviteListener(OnInviteListener onInviteListener) {
        this.onInviteListener = onInviteListener;
    }
}
