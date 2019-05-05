package com.webrtc.boyj.presentation.main;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserRepository;
import com.webrtc.boyj.presentation.BaseViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

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

    public void loadProfile(@NonNull final String id) {
        addDisposable(repository.getProfile(id)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturnItem(User.emptyUser())
                .flatMapCompletable(user -> {
                    if (user.isEmpty()) {
                        final User newUser = User.createFromId(id);
                        this.profile.setValue(newUser);
                        return repository.registerUser(newUser);
                    } else {
                        this.profile.setValue(user);
                        return repository.updateDeviceToken(id);
                    }
                }).subscribe(() -> { /* doNothing */ }, this.error::setValue));
    }

    public void loadNewUserList(@NonNull final String id) {
        addDisposable(repository.loadNewUserListExceptId(id)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> showLoading())
                .doFinally(this::hideLoading)
                .subscribe(this.otherUserList::setValue, this.error::setValue));
    }

    public void loadOtherUserList(@NonNull final String id) {
        addDisposable(repository.getOtherUserListExceptId(id)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> showLoading())
                .doFinally(this::hideLoading)
                .subscribe(this.otherUserList::setValue, this.error::setValue));
    }

    public void updateUserName(@NonNull final String id, @NonNull final String name) {
        addDisposable(repository.updateUserName(id, name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> this.profile.setValue(new User(id, name)),
                        this.error::setValue));
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
