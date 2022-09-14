package com.example.loggersample;

import android.app.Application;

import com.logger.logs.MyLogger;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MyLogger.init(getApplicationContext());
    }
}
