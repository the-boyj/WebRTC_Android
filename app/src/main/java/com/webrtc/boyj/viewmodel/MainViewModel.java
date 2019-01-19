package com.webrtc.boyj.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.webrtc.boyj.model.UserRepository;
import com.webrtc.boyj.model.dto.User;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainViewModel extends BaseViewModel {
    private UserRepository userRepository;
    private MutableLiveData<List<User>> users = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        userRepository = UserRepository.getInstance();

        addDisposable(userRepository.getUserList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> users.setValue(list)));
    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }
}
