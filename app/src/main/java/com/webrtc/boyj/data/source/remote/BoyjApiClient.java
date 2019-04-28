package com.webrtc.boyj.data.source.remote;

import com.webrtc.boyj.BuildConfig;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.remote.response.UserListResponse;
import com.webrtc.boyj.data.source.remote.response.UserResponse;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoyjApiClient implements BoyjApi {
    @NonNull
    private final Retrofit retrofit;

    @NonNull
    public static BoyjApiClient getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final BoyjApiClient INSTANCE = new BoyjApiClient();
    }

    private BoyjApiClient() {
        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getHttpLoggingClient());

        retrofit = retrofitBuilder.build();
    }

    private static OkHttpClient getHttpLoggingClient() {
        final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)).build();
        }
        return okHttpClientBuilder.build();
    }

    private BoyjApi getClient() {
        return retrofit.create(BoyjApi.class);
    }

    @Override
    public Single<UserResponse> getProfile(String userId) {
        return getClient().getProfile(userId);
    }

    @Override
    public Single<UserListResponse> getOthers(String userId) {
        return getClient().getOthers(userId);
    }

    @Override
    public Single<UserResponse> registerUser(User user) {
        return getClient().registerUser(user);
    }

    @Override
    public Single<UserResponse> updateDeviceToken(String userId, String token) {
        return getClient().updateDeviceToken(userId, token);
    }

    @Override
    public Single<UserResponse> updateUserName(String userId, String userName) {
        return getClient().updateUserName(userId, userName);
    }
}
