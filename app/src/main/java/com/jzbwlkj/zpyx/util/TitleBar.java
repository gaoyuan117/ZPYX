package com.jzbwlkj.zpyx.util;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzbwlkj.zpyx.R;


/**
 * Created by admin on 2017/2/16.
 */

public class TitleBar {
    private TextView mTitle;
    private ImageView mFinish;
    private ImageView mMore;
    private Activity context;

    public TitleBar(Activity context) {
        this.context = context;
        mTitle = (TextView) context.findViewById(R.id.m_title);
        mFinish = (ImageView) context.findViewById(R.id.m_finish);
        mMore = (ImageView) context.findViewById(R.id.m_more);
        setFinish();
    }

    /**
     * 设置标题
     *
     * @param str
     * @return
     */
    public TitleBar setTitle(String str) {
        mTitle.setText(str);
        return this;
    }

    /**
     * 设置标题栏左边按钮关闭
     *
     * @return
     */
    public TitleBar setFinish() {
        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
        return this;
    }

    /**
     * 设置右边按钮显示
     */
    public TitleBar showRight() {
        mMore.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置右边按钮点击事件
     *
     * @param listener
     * @return
     */
    public TitleBar setRightListener(View.OnClickListener listener) {
        mMore.setOnClickListener(listener);
        return this;
    }

    public TitleBar hideBack(){
        mFinish.setVisibility(View.INVISIBLE);
        return this;
    }

}
