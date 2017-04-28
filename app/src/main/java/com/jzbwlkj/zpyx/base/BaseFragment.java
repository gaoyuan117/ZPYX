package com.jzbwlkj.zpyx.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jzbwlkj.zpyx.activity.WebViewActivity;
import com.jzbwlkj.zpyx.config.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dn on 2017/2/10.
 */

public abstract class BaseFragment extends Fragment {

    public Activity mActivity;
    public Map<String,String> maps;
    private Intent intent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        maps = new HashMap<>();
        maps.put("token", Config.TOKEN);
        intent = new Intent(getActivity(), WebViewActivity.class);
        /**
         * 加载布局
         */
        View view = loadLayout(inflater);
        /**
         * 获取所有的主件
         */
        init(view);
        /**
         * 业务逻辑过程
         */
        initData();

        set();

        return view;
    }

    protected abstract View loadLayout(LayoutInflater inflater);

    protected abstract void init(View view);

    protected abstract void initData();

    protected abstract void set();

    public void toActivity(String url){
        if(url.contains("?")){
            intent.putExtra("url","http://zhaiwushuo.jzbwlkj.com/html/"+url+"&token="+Config.TOKEN);
        }else {
            intent.putExtra("url","http://zhaiwushuo.jzbwlkj.com/html/"+url+"?token="+Config.TOKEN);
        }
        startActivity(intent);
    }
}
