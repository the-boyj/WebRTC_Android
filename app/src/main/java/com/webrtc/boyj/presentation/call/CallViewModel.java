package com.webrtc.boyj.presentation.call;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.presentation.BaseViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CallViewModel extends BaseViewModel {
    @NonNull
    private final User otherUser;
    @NonNull
    private final ObservableBoolean isCalling = new ObservableBoolean(false);
    @NonNull
    private final ObservableInt callTime = new ObservableInt(0);

    @NonNull
    private final BoyjRTC boyjRTC;

    public CallViewModel(@NonNull User otherUser) {

        this.otherUser = otherUser;

        boyjRTC = new BoyjRTC();
    }

    public void call() {
        isCalling.set(true);

        addDisposable(Observable.interval(1, TimeUnit.SECONDS)
                .map(Long::intValue)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callTime::set));

        final DialPayload dialPayload = new DialPayload.Builder(otherUser.getDeviceToken()).build();
        boyjRTC.callAction(dialPayload);
    }

    public void hangUp() {
        boyjRTC.hangupAction();
    }

    @NonNull
    public User getOtherUser() {
        return otherUser;
    }

    @NonNull
    public ObservableBoolean getIsCalling() {
        return isCalling;
    }

    @NonNull
    public ObservableInt getCallTime() {
        return callTime;
    }
}
