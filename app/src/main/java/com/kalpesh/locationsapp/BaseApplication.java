package com.kalpesh.locationsapp;

import android.app.Application;

import okhttp3.OkHttpClient;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class BaseApplication extends Application {

    private static BaseApplication sInstance;
    private OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        okHttpClient = new OkHttpClient();
    }

    public static BaseApplication getInstance() {
        return sInstance;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
