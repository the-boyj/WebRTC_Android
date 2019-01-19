package com.webrtc.boyj.utils;

import android.util.Log;

public class Logger {
    public static void v(String msg) {
        Log.v(tag(), msg);
    }

    public static void d(String msg) {
        Log.d(tag(), msg);
    }

    public static void i(String msg) {
        Log.i(tag(), msg);
    }

    public static void w(String msg) {
        Log.w(tag(), msg);
    }

    public static void e(String msg) {
        Log.e(tag(), msg);
    }

    private static String tag() {
        int level = Thread.currentThread().getStackTrace().length;
        level = level >= 5 ? 4 : level-1;
        final StackTraceElement trace = Thread.currentThread().getStackTrace()[level];
        final String fileName = trace.getFileName();
        final String classPath = trace.getClassName();
        final String className = classPath.substring(classPath.lastIndexOf(".") + 1);
        final String methodName = trace.getMethodName();
        final int lineNumber = trace.getLineNumber();
        return String.format("BOYJ# %s.%s(%s:%s) ", className, methodName, fileName, lineNumber);
    }
}