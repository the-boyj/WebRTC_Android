package com.webrtc.boyj.presentation.call;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.webrtc.boyj.api.boyjrtc.BoyjRTC;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.boyjrtc.signalling.payload.DialPayload;
import com.webrtc.boyj.api.boyjrtc.BoyjMediaStream;
import com.webrtc.boyj.presentation.BaseViewModel;

import org.webrtc.MediaStream;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CallViewModel extends BaseViewModel {
    @NonNull
    private final ObservableInt callTime = new ObservableInt(0);
    @NonNull
    private ObservableBoolean isCalling = new ObservableBoolean();
    @NonNull
    private MutableLiveData<String> rejectedUserName = new MutableLiveData<>();
    @NonNull
    private MutableLiveData<MediaStream> localMediaStream = new MutableLiveData<>();
    @NonNull
    private MutableLiveData<List<BoyjMediaStream>> remoteMediaStreams = new MutableLiveData<>();
    @NonNull
    private MutableLiveData<String> leavedUserName = new MutableLiveData<>();
    @NonNull
    private MutableLiveData<Boolean> endOfCall = new MutableLiveData<>();
    @NonNull
    private final BoyjRTC boyjRTC;

    public CallViewModel(@NonNull BoyjRTC boyjRTC) {
        this.boyjRTC = boyjRTC;

        bindLocalStream();
        subscribeBoyjRTC();
    }

    private void bindLocalStream() {
        final MediaStream stream = boyjRTC.localStream();
        this.localMediaStream.setValue(stream);
    }

    private void subscribeBoyjRTC() {
        subscribeOnCall();
        subscribeOnReject();
        subscribeRemoteStreams();
        subscribeLeave();
        subscribeOnEndOfCall();
    }

    private void subscribeOnCall() {
        addDisposable(boyjRTC.onCalled()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::call));
    }

    private void call() {
        isCalling.set(true);

        addDisposable(Observable.interval(1, TimeUnit.SECONDS)
                .map(Long::intValue)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callTime::set));
    }

    private void subscribeOnReject() {
        addDisposable(boyjRTC.onRejected()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.rejectedUserName::setValue));
    }

    private void subscribeRemoteStreams() {
        addDisposable(boyjRTC.remoteStreams()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.remoteMediaStreams::setValue));
    }

    private void subscribeLeave() {
        addDisposable(boyjRTC.onLeaved()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userName -> this.leavedUserName.setValue(userName)));
    }

    private void subscribeOnEndOfCall() {
        addDisposable(boyjRTC.onEndOfCall()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> endOfCall.setValue(true)));
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
        endOfCall.setValue(true);
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
    public LiveData<MediaStream> getLocalMediaStream() {
        return localMediaStream;
    }

    @NonNull
    public LiveData<List<BoyjMediaStream>> getRemoteMediaStreams() {
        return remoteMediaStreams;
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
