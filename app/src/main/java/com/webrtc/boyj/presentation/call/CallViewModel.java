package com.webrtc.boyj.presentation.call;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.api.signalling.SignalingClient;
import com.webrtc.boyj.api.signalling.SignalingClientFactory;
import com.webrtc.boyj.api.signalling.SocketConnectionFailedException;
import com.webrtc.boyj.api.signalling.payload.DialPayload;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.presentation.BaseViewModel;
import com.webrtc.boyj.presentation.main.MainActivity;

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

    public CallViewModel(@NonNull User otherUser) {
        this.otherUser = otherUser;
    }

    public void call() {
        isCalling.set(true);

        addDisposable(Observable.interval(1, TimeUnit.SECONDS)
                .map(Long::intValue)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callTime::set));

        final DialPayload dialPayload = new DialPayload.Builder(otherUser.getDeviceToken()).build();


        SignalingClient signalingClient = null;
        try {
            signalingClient = SignalingClientFactory.getSignalingClient();
        } catch (SocketConnectionFailedException e) {
            //TODO 시그널링 서버 접속 에러를 사용자에게 알린후 재시도 유도
            e.printStackTrace();
            throw new RuntimeException();
        }

        signalingClient.emitDial(dialPayload);
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
