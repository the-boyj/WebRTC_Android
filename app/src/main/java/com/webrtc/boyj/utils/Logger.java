package com.webrtc.boyj.utils;

import android.util.Log;

public class Logger {
    private static final String TAG = "BOYJ";

    public static void v(String msg) {
        if (App.DEBUG) {
            Log.v(tag(), msg);
        }
    }

    public static void d(String msg) {
        if (App.DEBUG) {
            Log.d(tag(), msg);
        }
    }

    public static void i(String msg) {
        if (App.DEBUG) {
            Log.i(tag(), msg);
        }
    }

    public static void w(String msg) {
        if (App.DEBUG) {
            Log.w(tag(), msg);
        }
    }

    public static void e(String msg) {
        if (App.DEBUG) {
            Log.e(tag(), msg);
        }
    }

    private static String tag() {
        int level = Thread.currentThread().getStackTrace().length;
        level = level >= 5 ? 4 : level - 1;
        final StackTraceElement trace = Thread.currentThread().getStackTrace()[level];
        final String fileName = trace.getFileName();
        final String classPath = trace.getClassName();
        final String className = classPath.substring(classPath.lastIndexOf(".") + 1);
        final String methodName = trace.getMethodName();
        final int lineNumber = trace.getLineNumber();
        return String.format("%s# %s.%s(%s:%s) ", TAG, className, methodName, fileName, lineNumber);
    }
}