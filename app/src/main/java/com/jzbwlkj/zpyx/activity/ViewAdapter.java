package com.jzbwlkj.zpyx.activity;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by dn on 2017/2/14.
 */

public class ViewAdapter extends PagerAdapter {
    private List<View> mList;

    public ViewAdapter(List<View> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mList.get(position));
        return mList.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mList.get(position));// 移除当前的View
    }
}
