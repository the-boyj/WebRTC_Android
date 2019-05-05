package com.webrtc.boyj.presentation.call.invite;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserRepository;
import com.webrtc.boyj.presentation.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class InviteViewModel extends BaseViewModel {
    @NonNull
    private final UserRepository repository;
    @NonNull
    private final MutableLiveData<List<User>> otherUserList = new MutableLiveData<>();

    private InviteViewModel(@NonNull final UserRepository repository) {
        this.repository = repository;
    }

    public void loadOtherUserList(@NonNull final List<String> ids) {
        addDisposable(repository.getOtherUserListExceptIds(ids)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    final List<User> userList = new ArrayList<>(users);
                    this.otherUserList.setValue(userList);
                }, Throwable::printStackTrace)
        );
    }

    public LiveData<List<User>> getOtherUserList() {
        return otherUserList;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final UserRepository repository;

        public Factory(@NonNull final UserRepository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(InviteViewModel.class)) {
                return (T) new InviteViewModel(repository);
            } else {
                throw new IllegalArgumentException("ViewModel Not Found");
            }
        }
    }
}
