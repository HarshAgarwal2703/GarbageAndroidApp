package com.example.garbagecleanup;

import android.app.Application;

import com.example.garbagecleanup.helper.MySingleton;

/**
 * User: Aman
 * Date: 24-09-2019
 * Time: 09:31 AM
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MySingleton.init(this);
        PrefManager.init();
    }
}
