package com.webrtc.boyj.presentation.call.invite;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserRepository;
import com.webrtc.boyj.presentation.BaseViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InviteViewModel extends BaseViewModel {
    private final UserRepository repository;
    private final MutableLiveData<List<User>> otherUserList = new MutableLiveData<>();

    private InviteViewModel(@NonNull final UserRepository repository) {
        this.repository = repository;
    }

    public void init(@NonNull final String id) {
        subscribeOtherUserList(id);
    }

    private void subscribeOtherUserList(@NonNull final String id) {
        addDisposable(repository.getOtherUserList(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.otherUserList::setValue)
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
