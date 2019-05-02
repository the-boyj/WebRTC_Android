package com.webrtc.boyj;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.facebook.stetho.Stetho;

public class App extends Application {
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        // Browser : chrome://inspect/
        Stetho.initializeWithDefaults(this);
    }

    @NonNull
    public static Context getContext() {
        return application.getApplicationContext();
    }
}

