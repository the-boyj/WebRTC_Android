package com.webrtc.boyj.presentation.call;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.webrtc.boyj.presentation.common.viewmodel.BaseViewModel;
import com.webrtc.boyj.utils.SpeakerLoader;

public class SpeakerViewModel extends BaseViewModel {
    @NonNull
    private final SpeakerLoader speakerLoader;
    @NonNull
    private final ObservableBoolean isSpeakerphone = new ObservableBoolean();

    public SpeakerViewModel(@NonNull final SpeakerLoader speakerLoader) {
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
            if (modelClass.isAssignableFrom(SpeakerViewModel.class)) {
                return (T) new SpeakerViewModel(speakerLoader);
            } else {
                throw new IllegalArgumentException("ViewModel Not Found");
            }

        }
    }
}
