package com.webrtc.boyj.presentation.call;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.api.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.data.model.BoyjMediaStream;
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
    private final MutableLiveData<MediaStream> localMediaStream = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<BoyjMediaStream> remoteMediaStream = new MutableLiveData<>();
    @NonNull
    private final BoyjRTC boyjRTC;

    public CallViewModel(@NonNull final BoyjRTC boyjRTC) {
        this.boyjRTC = boyjRTC;
    }

    void init() {
        boyjRTC.initRTC();
        boyjRTC.startCapture();
        localMediaStream.setValue(boyjRTC.getUserMedia());

        addDisposable(boyjRTC.remoteMediaStream()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mediaStream -> {
                    call();
                    this.remoteMediaStream.setValue(mediaStream);
                }));
    }

    public void createRoom(@NonNull final String callerId) {
        final CreateRoomPayload payload = new CreateRoomPayload.Builder(callerId).build();
        boyjRTC.createRoom(payload);
    }

    public void dial(@NonNull final String calleeId) {
        final DialPayload dialPayload = new DialPayload.Builder(calleeId).build();
        boyjRTC.dial(dialPayload);
    }

    public void join() {
        // Todo : Offer 연결 필요 by accept
        boyjRTC.accept();
    }

    //전화 연결 되었을때 작업
    public void call() {
        isCalling.set(true);

        addDisposable(Observable.interval(1, TimeUnit.SECONDS)
                .map(Long::intValue)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callTime::set));

        addDisposable(boyjRTC.bye().subscribe(this::hangUp)
        );
    }

    void hangUp() {
        boyjRTC.hangUp();
    }

    @NonNull
    public ObservableBoolean getIsCalling() {
        return isCalling;
    }

    @NonNull
    public LiveData<MediaStream> getLocalMediaStream() {
        return localMediaStream;
    }

    @NonNull
    public LiveData<BoyjMediaStream> getRemoteMediaStream() {
        return remoteMediaStream;
    }

    @NonNull
    public ObservableInt getCallTime() {
        return callTime;
    }
}
