package com.jzbwlkj.zpyx.retrofit;

import com.jzbwlkj.zpyx.bean.CommonBean;
import com.jzbwlkj.zpyx.bean.DiscoverAdBean;
import com.jzbwlkj.zpyx.bean.DiscoverBean;
import com.jzbwlkj.zpyx.bean.GoodChooseBean;
import com.jzbwlkj.zpyx.bean.GuideBean;
import com.jzbwlkj.zpyx.bean.HomeBannerBean;
import com.jzbwlkj.zpyx.bean.LoginBean;
import com.jzbwlkj.zpyx.bean.RecommendBean;
import com.jzbwlkj.zpyx.bean.UserInfoBean;
import com.jzbwlkj.zpyx.config.HttpArray;
import com.jzbwlkj.zpyx.config.HttpResult;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by admin on 2017/3/27.
 */

public interface Api {

    /**
     * 获取启动页图片
     */
    @FormUrlEncoded
    @POST("api/base/startUpPic")
    Observable<HttpResult<GuideBean>> guidePic(@FieldMap Map<String, String> map);


    /**
     * 获取验证码
     */
    @FormUrlEncoded
    @POST("api/base/sendSms")
    Observable<HttpResult<CommonBean>> getCode(@FieldMap Map<String, String> map);

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("api/login/index")
    Observable<HttpResult<LoginBean>> login(@FieldMap Map<String, String> map);

    /**
     * 首页广告
     */
    @FormUrlEncoded
    @POST("api/index/ad")
    Observable<HttpArray<HomeBannerBean>> banner(@FieldMap Map<String, String> map);


    /**
     * 首页推荐
     */
    @FormUrlEncoded
    @POST("api/index/getRecommend")
    Observable<HttpArray<RecommendBean>> recommend(@FieldMap Map<String, String> map);

    /**
     * 首页优选
     */
    @FormUrlEncoded
    @POST("api/index/getGoodsList")
    Observable<HttpArray<GoodChooseBean>> goodChoose(@FieldMap Map<String, String> map);

    /**
     * 发现
     */
    @FormUrlEncoded
    @POST("api/log/find")
    Observable<HttpArray<DiscoverBean>> discoverFind(@FieldMap Map<String, String> map);

    /**
     * 发现页面广告
     */
    @FormUrlEncoded
    @POST("api/log/findAd")
    Observable<HttpArray<DiscoverAdBean>> discoverBanner(@FieldMap Map<String, String> map);

    /**
     * 是否有消息
     */
    @FormUrlEncoded
    @POST("api/index/hasNewMsg")
    Observable<HttpResult> hasNews(@FieldMap Map<String, String> map);

    /**
     * 获取用户信息
     */
    @FormUrlEncoded
    @POST("api/user/index")
    Observable<HttpResult<UserInfoBean>> getUserInfo(@FieldMap Map<String, String> map);

    /**
     * 获取单品收藏列表
     */
    @FormUrlEncoded
    @POST("api/Collection/getList")
    Observable<HttpArray<GoodChooseBean>> getSingleCollect(@FieldMap Map<String, String> map);

    /**
     * 获取日志收藏列表
     */
    @FormUrlEncoded
    @POST("api/Collection/getList")
    Observable<HttpArray<DiscoverBean>> getRzCollect(@FieldMap Map<String, String> map);

    /**
     * 分享成功
     */
    @FormUrlEncoded
    @POST("api/Integral/Share")
    Observable<HttpResult<CommonBean>> shareSuccess(@FieldMap Map<String, String> map);

}
