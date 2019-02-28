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
    private final MutableLiveData<List<User>> userList = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<User> myProfile = new MutableLiveData<>();
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
                    final List<User> userList = response.getUserList();
                    final User user = response.getMyProfile();
                    this.userList.setValue(userList);
                    this.myProfile.setValue(user);
                    unLoading();
                }, error::setValue));
    }

    void updateUserName(@NonNull final String tel,
                        @NonNull final String name) {
        loading();
        addDisposable(repository.updateUserName(tel, name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    final User newUser = new User(user.getName(),
                            user.getTel(),
                            user.getDeviceToken());
                    this.myProfile.setValue(newUser);
                    unLoading();
                }, error::setValue));
    }

    private void loading() {
        loading.set(true);
    }

    private void unLoading() {
        loading.set(false);
    }


    @NonNull
    public ObservableBoolean getLoading() {
        return loading;
    }

    @NonNull
    public LiveData<User> getMyProfile() {
        return myProfile;
    }

    @NonNull
    public LiveData<List<User>> getUserList() {
        return userList;
    }

    @NonNull
    MutableLiveData<Throwable> getError() {
        return error;
    }
}
