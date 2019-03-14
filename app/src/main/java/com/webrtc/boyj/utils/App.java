package com.webrtc.boyj.utils;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class App extends Application {

    @Nullable
    private static Application application;

    @NonNull
    private static Application getApplication() {
        return application;
    }

    @NonNull
    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}

