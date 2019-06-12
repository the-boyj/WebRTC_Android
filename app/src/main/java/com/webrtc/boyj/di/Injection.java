package com.webrtc.boyj.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.webrtc.boyj.api.boyjrtc.BoyjRTC;
import com.webrtc.boyj.data.source.TokenDataSource;
import com.webrtc.boyj.data.source.UserDataSource;
import com.webrtc.boyj.data.source.UserRepository;
import com.webrtc.boyj.data.source.UserRepositoryImpl;
import com.webrtc.boyj.data.source.local.preferences.TokenLocalDataSource;
import com.webrtc.boyj.data.source.local.room.AppDatabase;
import com.webrtc.boyj.data.source.local.room.UserLocalDataSource;
import com.webrtc.boyj.data.source.local.room.dao.UserDao;
import com.webrtc.boyj.data.source.remote.BoyjApi;
import com.webrtc.boyj.data.source.remote.BoyjApiClient;
import com.webrtc.boyj.data.source.remote.UserRemoteDataSource;
import com.webrtc.boyj.presentation.call.CallViewModel;
import com.webrtc.boyj.presentation.call.SpeakerViewModel;
import com.webrtc.boyj.presentation.call.invite.InviteViewModel;
import com.webrtc.boyj.presentation.main.MainViewModel;
import com.webrtc.boyj.presentation.ringing.RingingViewModel;
import com.webrtc.boyj.presentation.sign.SignViewModel;
import com.webrtc.boyj.utils.SpeakerLoader;

import java.util.Objects;

public class Injection {

    private static UserRepository providerUserRepository(@NonNull final Context context) {
        return UserRepositoryImpl.getInstance(
                providerLocalDataSource(context),
                providerRemoteDataSource(),
                providerTokenDataSource(context)
        );
    }

    private static UserDataSource providerLocalDataSource(@NonNull final Context context) {
        return UserLocalDataSource.getInstance(providerUserDao(context));
    }

    private static UserDao providerUserDao(@NonNull final Context context) {
        return AppDatabase.getInstance(context).userDao();
    }

    private static UserDataSource providerRemoteDataSource() {
        return UserRemoteDataSource.getInstance(providerBoyjApi());
    }

    private static BoyjApi providerBoyjApi() {
        return BoyjApiClient.getInstance();
    }

    public static TokenDataSource providerTokenDataSource(@NonNull final Context context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return TokenLocalDataSource.getInstance(pref);
    }

    public static ViewModelProvider.Factory providerSignViewModelFactory(@NonNull final Context context) {
        return new SignViewModel.Factory(providerTokenDataSource(context));
    }

    public static ViewModelProvider.Factory providerMainViewModelFactory(@NonNull final Context context) {
        return new MainViewModel.Factory(providerUserRepository(context));
    }

    public static ViewModelProvider.Factory providerCallViewModelFactory() {
        return new CallViewModel.Factory(new BoyjRTC());
    }

    public static ViewModelProvider.Factory providerRingingViewModelFactory(@NonNull Context context) {
        return new RingingViewModel.Factory(providerUserRepository(context));
    }

    public static ViewModelProvider.Factory providerSpeakerViewModelFactory(@NonNull Context context) {
        return new SpeakerViewModel.Factory(new SpeakerLoader(context));
    }

    public static ViewModelProvider.Factory providerInviteViewModelFactory(@Nullable Context context) {
        return new InviteViewModel.Factory(providerUserDao(Objects.requireNonNull(context)));
    }
}
