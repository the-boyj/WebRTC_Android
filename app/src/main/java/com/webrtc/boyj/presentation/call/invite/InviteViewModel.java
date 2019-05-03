package com.webrtc.boyj.presentation.call.invite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserRepository;
import com.webrtc.boyj.presentation.BaseViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
