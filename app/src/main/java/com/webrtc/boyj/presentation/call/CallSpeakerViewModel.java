package com.webrtc.boyj.presentation.call;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.webrtc.boyj.presentation.BaseViewModel;
import com.webrtc.boyj.utils.SpeakerLoader;

public class CallSpeakerViewModel extends BaseViewModel {
    private final SpeakerLoader speakerLoader;
    private final ObservableBoolean isSpeakerphone = new ObservableBoolean();

    public CallSpeakerViewModel(SpeakerLoader speakerLoader) {
        this.speakerLoader = speakerLoader;
        turnOnSpeaker();
    }

    public void toggleSpeaker() {
        if (isSpeakerphone.get()) {
            turnOffSpeaker();
        } else {
            turnOnSpeaker();
        }
    }

    private void turnOnSpeaker() {
        speakerLoader.turnOn();
        isSpeakerphone.set(true);
    }

    private void turnOffSpeaker() {
        speakerLoader.turnOff();
        isSpeakerphone.set(false);
    }

    public ObservableBoolean getIsSpeakerphone() {
        return isSpeakerphone;
    }

    @Override
    protected void onCleared() {
        speakerLoader.turnOff();
        super.onCleared();
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final SpeakerLoader speakerLoader;

        public Factory(SpeakerLoader speakerLoader) {
            this.speakerLoader = speakerLoader;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CallSpeakerViewModel.class)) {
                return (T) new CallSpeakerViewModel(speakerLoader);
            } else {
                throw new IllegalArgumentException("ViewModel Not Found");
            }
        }
    }
}
