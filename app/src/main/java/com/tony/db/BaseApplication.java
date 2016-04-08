package com.tony.db;

import android.app.Application;

/**
 * Created by tony on 16/4/5.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBManager.getInstance(BaseApplication.this);
            }
        }).start();
    }
}
