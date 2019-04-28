package com.webrtc.boyj.presentation.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserRepository;
import com.webrtc.boyj.presentation.BaseViewModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends BaseViewModel {
    @NonNull
    private final MutableLiveData<User> profile = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<List<User>> otherUserList = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<Throwable> error = new MutableLiveData<>();
    @NonNull
    private final ObservableBoolean loading = new ObservableBoolean();
    @NonNull
    private final UserRepository repository;

    public MainViewModel(@NonNull UserRepository repository) {
        this.repository = repository;
    }

    public void init(@NonNull final String id) {
        loadProfile(id);
        loadOtherUserList(id);
    }

    private void loadProfile(@NonNull final String id) {
        addDisposable(repository.getProfile(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(this.profile::setValue)
                .flatMapCompletable(__ -> updateDeviceToken(id))
                .subscribe(() -> { /* doNothing */ }, this.error::setValue)
        );
    }

    private Completable updateDeviceToken(@NonNull final String id) {
        return repository.updateDeviceToken(id).subscribeOn(Schedulers.io());
    }

    private void loadOtherUserList(@NonNull final String id) {
        addDisposable(repository.getOtherUserList(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> showLoading())
                .doFinally(this::hideLoading)
                .subscribe(this.otherUserList::setValue, this.error::setValue)
        );
    }

    public void updateUserName(@NonNull final String id, @NonNull final String name) {
        addDisposable(repository.updateUserName(id, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> this.profile.setValue(new User(id, name)), // onComplete
                        this.error::setValue) // onError
        );
    }

    private void showLoading() {
        loading.set(true);
    }

    private void hideLoading() {
        loading.set(false);
    }

    @NonNull
    public LiveData<User> getProfile() {
        return profile;
    }

    @NonNull
    public LiveData<List<User>> getOtherUserList() {
        return otherUserList;
    }

    @NonNull
    public LiveData<Throwable> getError() {
        return error;
    }

    @NonNull
    public ObservableBoolean getLoading() {
        return loading;
    }
}
