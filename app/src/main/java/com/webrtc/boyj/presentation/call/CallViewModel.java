package com.webrtc.boyj.presentation.call;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.webrtc.boyj.api.boyjrtc.BoyjMediaStream;
import com.webrtc.boyj.api.boyjrtc.BoyjRTC;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.DialPayload;
import com.webrtc.boyj.presentation.BaseViewModel;

import org.webrtc.MediaStream;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CallViewModel extends BaseViewModel {
    @NonNull
    private final ObservableInt callTime = new ObservableInt(0);
    @NonNull
    private final ObservableBoolean isCalling = new ObservableBoolean();
    @NonNull
    private final MutableLiveData<MediaStream> localStream = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<BoyjMediaStream> remoteStream = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<String> rejectedUserName = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<String> leavedUserName = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<Boolean> endOfCall = new MutableLiveData<>();
    @NonNull
    private final BoyjRTC boyjRTC;

    public CallViewModel(@NonNull final BoyjRTC boyjRTC) {
        this.boyjRTC = boyjRTC;
        subscribeBoyjRTC();
    }

    public void initLocalStream() {
        boyjRTC.startCapture();
        final MediaStream mediaStream = boyjRTC.localStream();
        this.localStream.setValue(mediaStream);
    }

    private void subscribeBoyjRTC() {
        subscribeOnReject();
        subscribeRemoteStream();
        subscribeLeave();
        subscribeOnCallFinish();
    }

    private void subscribeOnReject() {
        addDisposable(boyjRTC.onRejected()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.rejectedUserName::setValue,
                        Throwable::printStackTrace));
    }

    private void subscribeRemoteStream() {
        addDisposable(boyjRTC.remoteStream()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stream -> {
                    call();
                    remoteStream.setValue(stream);
                }, Throwable::printStackTrace));
    }

    public void call() {
        if (!isCalling.get()) {
            isCalling.set(true);
            addDisposable(Observable.interval(1, TimeUnit.SECONDS)
                    .map(Long::intValue)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callTime::set, Throwable::printStackTrace));
        }
    }

    private void subscribeLeave() {
        addDisposable(boyjRTC.onLeaved()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(name -> {
                    leavedUserName.setValue(name);
                    Log.d("BOYJ", name);
                }, Throwable::printStackTrace));
    }

    private void subscribeOnCallFinish() {
        addDisposable(boyjRTC.onCallFinish()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            this.endOfCall.setValue(true);
                        },
                        Throwable::printStackTrace));
    }

    public void initCaller(@NonNull final String callerId,
                           @NonNull final String calleeId) {
        createRoom(callerId);
        dial(calleeId);
    }

    private void createRoom(@NonNull final String callerId) {
        final CreateRoomPayload payload = new CreateRoomPayload(callerId);
        boyjRTC.createRoom(payload);
    }

    private void dial(@NonNull String calleeId) {
        final DialPayload payload = new DialPayload(calleeId);
        boyjRTC.dial(payload);
    }

    public void invite(@NonNull final String calleeId) {
        final DialPayload payload = new DialPayload(calleeId);
        boyjRTC.dial(payload);
    }

    public void initCallee() {
        boyjRTC.accept();
    }

    public void hangUp() {
        boyjRTC.endOfCall();
    }

    @NonNull
    public ObservableInt getCallTime() {
        return callTime;
    }

    @NonNull
    public ObservableBoolean getIsCalling() {
        return isCalling;
    }

    @NonNull
    public LiveData<String> getRejectedUserName() {
        return rejectedUserName;
    }

    @NonNull
    public LiveData<MediaStream> getLocalStream() {
        return localStream;
    }

    @NonNull
    public LiveData<BoyjMediaStream> getRemoteStream() {
        return remoteStream;
    }

    @NonNull
    public LiveData<String> getLeavedUserName() {
        return leavedUserName;
    }

    @NonNull
    public LiveData<Boolean> getEndOfCall() {
        return endOfCall;
    }
}
