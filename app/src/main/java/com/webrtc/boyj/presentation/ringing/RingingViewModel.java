package com.webrtc.boyj.presentation.ringing;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.webrtc.boyj.api.boyjrtc.BoyjRTC;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.RejectPayload;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.UserRepository;
import com.webrtc.boyj.presentation.common.viewmodel.BaseViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class RingingViewModel extends BaseViewModel {
    @NonNull
    private final MutableLiveData<User> caller = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<Throwable> error = new MutableLiveData<>();
    @NonNull
    private final UserRepository repository;
    @NonNull
    private final BoyjRTC boyjRTC;

    public RingingViewModel(@NonNull final UserRepository repository) {
        this.repository = repository;
        this.boyjRTC = new BoyjRTC();
    }

    public void loadCallerProfile(@NonNull final String callerId) {
        addDisposable(repository.getProfile(callerId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.caller::setValue, this.error::setValue));
    }

    public void awaken(@NonNull final String room,
                       @NonNull final String callerId,
                       @NonNull final String calleeId) {
        boyjRTC.awaken(new AwakenPayload(room, callerId, calleeId));
    }

    public void reject(@NonNull final String callerId) {
        final RejectPayload payload = new RejectPayload();
        payload.setReceiver(callerId);
        boyjRTC.reject(payload);
    }

    @NonNull
    public LiveData<User> getCaller() {
        return caller;
    }

    @NonNull
    public LiveData<Throwable> getError() {
        return error;
    }

    public static class Factory implements ViewModelProvider.Factory {
        @NonNull
        private final UserRepository repository;

        public Factory(@NonNull UserRepository repository) {
            this.repository = repository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RingingViewModel.class)) {
                return (T) new RingingViewModel(repository);
            } else {
                throw new IllegalArgumentException("ViewModel Not Found");
            }
        }
    }
}
