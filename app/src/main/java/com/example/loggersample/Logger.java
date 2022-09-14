package com.example.loggersample;

import android.util.Log;

import com.logger.logs.MyLogger;

public class Logger {

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
        MyLogger.trace(msg);
    }

    public static void e(String tag, String msg) {
        Log.d(tag, msg);
        MyLogger.trace(msg);
    }

    public static void i(String tag, String msg) {
        Log.d(tag, msg);
        MyLogger.trace(msg);
    }

}
