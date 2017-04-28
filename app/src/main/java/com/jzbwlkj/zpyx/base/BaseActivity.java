package com.jzbwlkj.zpyx.base;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.jzbwlkj.zpyx.config.Config;
import com.jzbwlkj.zpyx.util.AppManager;
import com.trello.rxlifecycle2.components.RxActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;


/**
 * Created by admin on 2017/3/29.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    public Map<String, String> maps;//传递参数


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initState();
        mProgressDialog = new ProgressDialog(this);//这里要用this，否则报错
        maps = new HashMap<>();
        if (!TextUtils.isEmpty(Config.TOKEN)) {//Token
            maps.put("token", Config.TOKEN);
        }
        init();
        initData();
        setData();
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
    }


    protected abstract void init();//初始化

    protected abstract void initData();//初始化数据

    protected abstract void setData();//设置

    /**
     * 沉浸式状态栏
     */
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 显示加载对话框
     *
     * @param message
     */
    protected void showProgressDialog(String message) {
        mProgressDialog.setMessage(message);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    /**
     * 隐藏加载对话框
     */
    protected void dismissProgressDialog() {
        mProgressDialog.dismiss();
    }

    /**
     * 跳转页面
     */
    protected void toActivity(Class<?> activity) {
        startActivity(new Intent(this, activity));
    }

    /**
     * 携带数据的跳转
     *
     * @param intent
     */
    protected void toIntentActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
