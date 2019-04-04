package com.webrtc.boyj.presentation.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.repository.UserRepository;
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

    void init(@NonNull final String tel) {
        loading();
        addDisposable(repository.updateToken(tel)
                .andThen(repository.getUserList(tel))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    final List<User> userList = response.getOtherUserList();
                    final User user = response.getUser();
                    this.otherUserList.setValue(userList);
                    this.user.setValue(user);
                    unLoading();
                }, e -> {
                    unLoading();
                    error.setValue(e);
                }));
    }

    void updateUserName(@NonNull final String tel,
                        @NonNull final String name) {
        loading();
        addDisposable(repository.updateUserName(tel, name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    final User newUser = new User(user.getTel(),
                            user.getName(),
                            user.getDeviceToken());
                    this.user.setValue(newUser);
                    unLoading();
                }, e -> {
                    unLoading();
                    error.setValue(e);
                }));
    }

    private void loading() {
        loading.set(true);
    }

    private void unLoading() {
        loading.set(false);
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
