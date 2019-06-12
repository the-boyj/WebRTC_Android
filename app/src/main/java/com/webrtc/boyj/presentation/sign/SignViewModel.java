package com.webrtc.boyj.presentation.sign;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.webrtc.boyj.data.source.TokenDataSource;
import com.webrtc.boyj.presentation.common.Event;
import com.webrtc.boyj.presentation.common.viewmodel.BaseViewModel;

public class SignViewModel extends BaseViewModel {
    @NonNull
    private final ObservableField<String> idField = new ObservableField<>();
    @NonNull
    private final ObservableBoolean validId = new ObservableBoolean(true);
    @NonNull
    private final MutableLiveData<Event<String>> signInEvent = new MutableLiveData<>();
    @NonNull
    private final TokenDataSource dataSource;

    public SignViewModel(@NonNull TokenDataSource dataSource) {
        this.dataSource = dataSource;

        idField.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (isValidId(idField.get())) {
                    validId.set(true);
                }
            }
        });
    }

    public void setIdField(@Nullable final String idField) {
        if (isValidId(idField)) {
            this.idField.set(idField);
        }
    }

    public void signInClicked() {
        final String id = this.idField.get();
        if (isValidId(id)) {
            signInEvent.setValue(new Event<>(id));
        } else {
            validId.set(false);
        }
    }

    private boolean isValidId(@Nullable final String id) {
        return !TextUtils.isEmpty(id);
    }

    public void checkAndSetNewTokenStatus(@NonNull final String savedId) {
        if (TextUtils.isEmpty(savedId) || !savedId.equals(idField.get())) {
            dataSource.setNewToken();
        }
    }

    @NonNull
    public ObservableField<String> getIdField() {
        return idField;
    }

    @NonNull
    public ObservableBoolean getValidId() {
        return validId;
    }

    @NonNull
    public LiveData<Event<String>> getSignInEvent() {
        return signInEvent;
    }

    public static class Factory implements ViewModelProvider.Factory {
        @NonNull
        private TokenDataSource dataSource;

        public Factory(@NonNull TokenDataSource dataSource) {
            this.dataSource = dataSource;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(SignViewModel.class)) {
                return (T) new SignViewModel(dataSource);
            } else {
                throw new IllegalArgumentException("ViewModel Not Found");
            }
        }
    }
}
