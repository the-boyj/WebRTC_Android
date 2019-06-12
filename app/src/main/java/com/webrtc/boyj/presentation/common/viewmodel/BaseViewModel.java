package com.webrtc.boyj.presentation.common.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseViewModel extends ViewModel {
    @NonNull
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void addDisposable(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
