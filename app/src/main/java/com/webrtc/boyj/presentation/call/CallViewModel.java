package com.webrtc.boyj.presentation.call;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.api.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
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
    private final MutableLiveData<MediaStream> remoteMediaStream = new MutableLiveData<>();
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

    public void createRoom(@NonNull final CreateRoomPayload payload) {
        boyjRTC.createRoom(payload);
    }

    //전화 거는 요청
    void dial(@NonNull final String room) {
        boyjRTC.readyToCall(true);
        final DialPayload dialPayload = new DialPayload.Builder(room).build();
        boyjRTC.dial(dialPayload);
    }

    void join() {
        boyjRTC.readyToCall(false);
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
    public MutableLiveData<MediaStream> getLocalMediaStream() {
        return localMediaStream;
    }

    @NonNull
    public MutableLiveData<MediaStream> getRemoteMediaStream() {
        return remoteMediaStream;
    }

    @NonNull
    public ObservableInt getCallTime() {
        return callTime;
    }
}
