package com.webrtc.boyj.presentation.call;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.api.signalling.payload.EndOfCallPayload;
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
    private final MutableLiveData<Boolean> isEnded = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<String> rejectedUserName = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<String> byeUserName = new MutableLiveData<>();
    private BoyjRTC boyjRTC;

    public CallViewModel() {
        boyjRTC = new BoyjRTC();
    }

    public void init() {
        boyjRTC.initRTC();
        boyjRTC.startCapture();

        localMediaStream.setValue(boyjRTC.getLocalStream());

        addDisposable(boyjRTC.getRemoetStreamSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mediaStream -> {
                    if (!isCalling.get()) { // 최초 통화의 경우 타이머 작동
                        call();
                    }
                    this.remoteMediaStream.setValue(mediaStream);
                })
        );

        addDisposable(boyjRTC.getRejectSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (!isCalling.get()) { // 최초 통화 거부
                        isEnded.setValue(true);
                    } else { // 기존 통화 중 거부
                        rejectedUserName.setValue(response.getSender());
                    }
                })
        );

        addDisposable(boyjRTC.getEndOfCallSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .map(EndOfCallPayload::getSender)
                .subscribe(byeUserName::setValue)
        );

    }

    public void dial(@NonNull final String calleeId) {
        final DialPayload dialPayload = new DialPayload.Builder(calleeId).build();
        boyjRTC.dial(dialPayload);
    }

    public void createRoom(@NonNull final String callerId) {
        final CreateRoomPayload payload = new CreateRoomPayload.Builder(callerId).build();
        boyjRTC.createRoom(payload);
    }

    public void accept(@NonNull final String callerId) {
        boyjRTC.accept(callerId);
    }

    /**
     * 최초 전화연결 이후 호출
     */
    private void call() {
        isCalling.set(true);

        addDisposable(Observable.interval(1, TimeUnit.SECONDS)
                .map(Long::intValue)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callTime::set));
    }

    /**
     * 사용자가 전화를 끊는 경우 END_OF_CALL 이벤트를 시그널링 서버로 송신 후 release 한다.
     */
    public void hangUp() {
        boyjRTC.endOfCall();
        endOfCall();
    }

    /**
     * 모든 통화가 종료되었을때 호출
     * 1. 통화중인 유저가 모두 나갔을 경우
     * 2. 사용자가 통화 종료를 누른 경우
     */
    public void endOfCall() {
        boyjRTC.release();
        isEnded.setValue(true);
    }

    @NonNull
    public ObservableBoolean getIsCalling() {
        return isCalling;
    }

    @NonNull
    public LiveData<Boolean> getIsEnded() {
        return isEnded;
    }

    @NonNull
    public LiveData<String> getRejectedUserName() {
        return rejectedUserName;
    }

    @NonNull
    public LiveData<String> getByeUserName() {
        return byeUserName;
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
