package com.webrtc.boyj.utils;

import android.util.Log;

public class Logg {
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
        int level = 4;
        StackTraceElement trace = Thread.currentThread().getStackTrace()[level];
        String fileName = trace.getFileName();
        String classPath = trace.getClassName();
        String className = classPath.substring(classPath.lastIndexOf(".") + 1);
        String methodName = trace.getMethodName();
        int lineNumber = trace.getLineNumber();
        return "BOYJ# " + className + "." + methodName + "(" + fileName + ":" + lineNumber + ")";
    }
}