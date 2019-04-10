package com.webrtc.boyj.utils;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

public class App extends Application {
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    @NonNull
    public static Context getContext() {
        return application.getApplicationContext();
    }
}

