package com.jzbwlkj.zpyx.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jzbwlkj.zpyx.R;
import com.jzbwlkj.zpyx.bean.GoodChooseBean;
import com.jzbwlkj.zpyx.util.GlideUtils;
import com.jzbwlkj.zpyx.view.RoundImageView;

import java.util.List;

/**
 * Created by admin on 2017/4/11.
 */

public class HomeAdapter extends BaseQuickAdapter<GoodChooseBean,BaseViewHolder>{

    public HomeAdapter(int layoutResId, List<GoodChooseBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodChooseBean item) {
        helper.setText(R.id.tv_item_home_title,item.getGoods_name())
                .setText(R.id.tv_item_home_money,"￥"+item.getGoods_price())
                .setText(R.id.tv_item_home_from,item.getGoods_source());
        GlideUtils.glide(item.getGoods_pic(), (RoundImageView) helper.getView(R.id.img_item_home));
    }
}
