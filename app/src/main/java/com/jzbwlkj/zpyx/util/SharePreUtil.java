package com.jzbwlkj.zpyx.util;

import android.content.SharedPreferences;

import com.jzbwlkj.zpyx.App;


/**
 * Created by dn on 2017/2/20.
 * <p>
 * SharedPreferences 工具类
 */

public class SharePreUtil {
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor edit;

    public SharePreUtil(String key) {
        sharedPreference = App.app.getSharedPreferences(key, App.app.MODE_PRIVATE);
    }

    /**
     * 初始化edit()
     *
     * @return
     */
    public SharePreUtil edit() {
        edit = sharedPreference.edit();
        return this;
    }

    /**
     * 保存
     *
     * @param key
     * @param value
     * @return
     */
    public SharePreUtil putString(String key, String value) {
        edit.putString(key, value);
        return this;
    }

    /**
     * 提交
     *
     * @return
     */
    public SharePreUtil commit() {
        edit.commit();
        return this;
    }

    /**
     * 获取数据
     * @param key
     * @return
     */
    public String get(String key) {
        String value = sharedPreference.getString(key,"");
        return value;
    }

}
