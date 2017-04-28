package com.jzbwlkj.zpyx.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jzbwlkj.zpyx.R;
import com.jzbwlkj.zpyx.bean.DiscoverBean;
import com.jzbwlkj.zpyx.util.GlideUtils;

import java.util.List;

/**
 * Created by admin on 2017/4/12.
 */

public class DiscoverAdapter extends BaseQuickAdapter<DiscoverBean,BaseViewHolder>{

    public DiscoverAdapter(int layoutResId, List<DiscoverBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscoverBean item) {
        helper.setText(R.id.tv_item_discover_name,item.getUser_nickname())
                .setText(R.id.tv_item_discover_title,item.getLog_title())
                .setText(R.id.tv_item_discover_place,item.getProvince()+" "+item.getCity());
        GlideUtils.glideAvatar(item.getUser_portrait(), (ImageView) helper.getView(R.id.img_item_discover_avatar));
        GlideUtils.glide(item.getLog_pic(), (ImageView) helper.getView(R.id.img_item_discover_bg));
    }
}
