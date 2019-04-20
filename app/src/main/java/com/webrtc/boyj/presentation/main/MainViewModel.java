package com.webrtc.boyj.presentation.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserRepository;
import com.webrtc.boyj.presentation.BaseViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainViewModel extends BaseViewModel {
    @NonNull
    private final MutableLiveData<User> user = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<List<User>> otherUserList = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<Throwable> error = new MutableLiveData<>();
    @NonNull
    private final ObservableBoolean loading = new ObservableBoolean(true);
    @NonNull
    private final UserRepository repository;

    MainViewModel(@NonNull UserRepository repository) {
        this.repository = repository;
    }

    void init(@NonNull final String id) {
        loading.set(true);

        addDisposable(repository.getUser(id)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapCompletable(user -> {
                    this.user.setValue(user);
                    return repository.updateDeviceToken(id);
                }).subscribe(() -> { /* doNothing */ }, this.error::setValue)
        );

        addDisposable(repository.getOtherUserList(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userList -> {
                    loading.set(false);
                    this.otherUserList.setValue(userList);
                }, error -> {
                    loading.set(false);
                    this.error.setValue(error);
                })
        );
    }

    void updateUserName(@NonNull final String id,
                        @NonNull final String name) {
        addDisposable(repository.updateUserName(id, name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    final User newUser = new User(id, name, null);
                    this.user.setValue(newUser);
                }, this.error::setValue)
        );
    }

    @NonNull
    public LiveData<User> getUser() {
        return user;
    }

    @NonNull
    public LiveData<List<User>> getOtherUserList() {
        return otherUserList;
    }

    @NonNull
    MutableLiveData<Throwable> getError() {
        return error;
    }

    @NonNull
    public ObservableBoolean getLoading() {
        return loading;
    }
}
