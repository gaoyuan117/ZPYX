package com.jzbwlkj.zpyx;

import android.app.Application;
import android.content.Context;

import com.jzbwlkj.zpyx.bean.GoodChooseBean;
import com.jzbwlkj.zpyx.bean.HomeBannerBean;
import com.jzbwlkj.zpyx.bean.RecommendBean;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/4/11.
 */

public class App extends Application {
    public static Context app;
    public static List<String> bannerList;
    public static List<RecommendBean> recommenflist;
    public static List<GoodChooseBean> goodsList;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        bannerList = new ArrayList<>();
        recommenflist = new ArrayList<>();
        goodsList = new ArrayList<>();
        Logger.init("OkHttp")
                .methodCount(1) // 方法栈打印的个数，默认是 2
                .hideThreadInfo(); // // 隐藏线程信息，默认显示
    }
}
