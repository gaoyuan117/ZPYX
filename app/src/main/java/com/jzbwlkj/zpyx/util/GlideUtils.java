package com.jzbwlkj.zpyx.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jzbwlkj.zpyx.App;
import com.jzbwlkj.zpyx.R;
import com.jzbwlkj.zpyx.config.Config;
import com.jzbwlkj.zpyx.view.GlideRoundTransform;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by admin on 2017/3/30.
 */

public class GlideUtils {

    public static void glide(String imgPath, ImageView imageView) {
        Glide.with(App.app)
                .load(Config.BASE_URL + imgPath)
                .crossFade()
                .error(R.mipmap.defaultpic)
                .into(imageView);
    }

    public static void glideAvatar(String imgPath, ImageView imageView) {
        Glide.with(App.app)
                .load(Config.BASE_URL + imgPath)
                .crossFade()
                .error(R.mipmap.logo2)
                .into(imageView);
    }

}
