package com.daisynowhere.gohere;

/**
 * Created by daisy on 5/24/16.
 */

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
    }

}