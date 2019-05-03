package com.webrtc.boyj.data.source.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.webrtc.boyj.BuildConfig;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.remote.response.ListResponse;
import com.webrtc.boyj.data.source.remote.response.Response;

import io.reactivex.Completable;
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
            okHttpClientBuilder
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)).build();
        }
        return okHttpClientBuilder.build();
    }

    private BoyjApi getClient() {
        return retrofit.create(BoyjApi.class);
    }

    @Override
    public Single<Response<User>> getProfile(String userId) {
        return getClient().getProfile(userId);
    }

    @Override
    public Single<ListResponse<User>> getOthers(String userId) {
        return getClient().getOthers(userId);
    }

    @Override
    public Completable registerUser(User user) {
        return getClient().registerUser(user);
    }

    @Override
    public Completable updateDeviceToken(String userId, String token) {
        return getClient().updateDeviceToken(userId, token);
    }

    @Override
    public Completable updateUserName(String userId, String name) {
        return getClient().updateUserName(userId, name);
    }
}
