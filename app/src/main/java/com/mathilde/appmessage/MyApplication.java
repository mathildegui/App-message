package com.mathilde.appmessage;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * @author mathilde on 15/02/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
