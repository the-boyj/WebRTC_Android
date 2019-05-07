package com.webrtc.boyj.presentation.call.invite;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.local.room.dao.UserDao;
import com.webrtc.boyj.data.source.local.room.entity.UserMapper;
import com.webrtc.boyj.presentation.BaseViewModel;

import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InviteViewModel extends BaseViewModel {
    @NonNull
    private final UserDao userDao;
    @NonNull
    private final MutableLiveData<List<User>> otherUserList = new MutableLiveData<>();

    public InviteViewModel(@NonNull UserDao userDao) {
        this.userDao = userDao;
    }

    public void loadOtherUserList(@NonNull final List<String> ids) {
        addDisposable(userDao.selectExceptIds(ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(UserMapper::toUserListFromEntities)
                .onErrorReturnItem(Collections.emptyList())
                .subscribe(this.otherUserList::setValue, Throwable::printStackTrace)
        );
    }

    public LiveData<List<User>> getOtherUserList() {
        return otherUserList;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final UserDao userDao;

        public Factory(UserDao userDao) {
            this.userDao = userDao;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(InviteViewModel.class)) {
                return (T) new InviteViewModel(userDao);
            } else {
                throw new IllegalArgumentException("ViewModel Not Found");
            }
        }
    }
}
