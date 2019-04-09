package com.webrtc.boyj.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

public class App extends Application {
    private static Application application;
    public static boolean DEBUG = false;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        DEBUG = isDebuggable(this);
    }

    @NonNull
    public static Context getContext() {
        return application.getApplicationContext();
    }

    private boolean isDebuggable(Context context) {
        boolean debuggable = false;

        final PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), 0);
            debuggable = (0 != (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
            /* debuggable variable will remain false */
        }

        return debuggable;
    }
}

