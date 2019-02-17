package com.webrtc.boyj.presentation.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.repository.UserRepository;
import com.webrtc.boyj.presentation.BaseViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainViewModel extends BaseViewModel {
    @NonNull
    private final UserRepository repository;
    @NonNull
    private final MutableLiveData<User> myUser = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<List<User>> userList = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<Throwable> error = new MutableLiveData<>();
    @NonNull
    private final String tel;

    MainViewModel(@NonNull UserRepository repository,
                  @NonNull String tel) {
        this.repository = repository;
        this.tel = tel;
    }

    void init() {
        addDisposable(repository.updateToken(tel)
                .andThen(repository.getUserList(tel))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    final List<User> userList = response.getUserList();
                    final User user = response.getMyUser();
                    this.userList.setValue(userList);
                    this.myUser.setValue(user);
                }, error::setValue));
    }

    void updateUserName(@NonNull String name) {
        addDisposable(repository.updateUserName(tel, name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    final User newUser = new User(user.getName(),
                            user.getTel(),
                            user.getDeviceToken());
                    this.myUser.setValue(newUser);
                }, error::setValue));
    }

    @NonNull
    public LiveData<User> getMyUser() {
        return myUser;
    }

    @NonNull
    public LiveData<List<User>> getUserList() {
        return userList;
    }
}
