package com.webrtc.boyj.presentation.ringing;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.webrtc.boyj.api.boyjrtc.BoyjRTC;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.AwakenPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.RejectPayload;
import com.webrtc.boyj.data.source.UserRepository;
import com.webrtc.boyj.presentation.BaseViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class RingingViewModel extends BaseViewModel {
    @NonNull
    private final MutableLiveData<String> callerId = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<String> callerName = new MutableLiveData<>();
    @NonNull
    private final UserRepository repository;
    private BoyjRTC boyjRTC;

    public RingingViewModel(@NonNull final UserRepository repository) {
        this.repository = repository;
        this.boyjRTC = new BoyjRTC();
    }

    public void loadCallerProfile(@NonNull final String callerId) {
        addDisposable(repository.getProfile(callerId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    this.callerId.setValue(user.getId());
                    this.callerName.setValue(user.getName());
                }));
    }

    public void awaken(@NonNull final String room,
                       @NonNull final String callerId,
                       @NonNull final String calleeId) {
        final AwakenPayload payload = new AwakenPayload(room, callerId, calleeId);
        boyjRTC.awaken(payload);
    }

    public void reject(@NonNull final String callerId) {
        final RejectPayload payload = new RejectPayload();
        payload.setReceiver(callerId);
        boyjRTC.reject(payload);
    }

    @NonNull
    public LiveData<String> getCallerId() {
        return callerId;
    }

    @NonNull
    public MutableLiveData<String> getCallerName() {
        return callerName;
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
