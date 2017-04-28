package com.jzbwlkj.zpyx;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.Logger;

/**
 * Created by admin on 2017/4/11.
 */

public class App extends Application{
    public static Context app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Logger.init("OkHttp")
                .methodCount(1) // 方法栈打印的个数，默认是 2
                .hideThreadInfo(); // // 隐藏线程信息，默认显示
    }
}
