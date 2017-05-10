package com.jzbwlkj.zpyx.util;

import android.content.Context;

import com.jzbwlkj.zpyx.R;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by admin on 2017/4/14.
 */

public class ShareUtils {

    public static OnekeyShare share(Context context, String url) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("买手大叔");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("买手大叔欢迎你");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        oks.setImageUrl("http://zhaiwushuo.jzbwlkj.com/logo2.jpg"); // 必须有图片路径才能在微信分享有标题
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("买手大叔");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://zhaiwushuo.jzbwlkj.com/logo2.jpg");
        // 启动分享GUI
        oks.show(context);
        return oks;
    }
}
