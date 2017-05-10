package com.jzbwlkj.zpyx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jzbwlkj.zpyx.App;
import com.jzbwlkj.zpyx.R;
import com.jzbwlkj.zpyx.base.BaseActivity;
import com.jzbwlkj.zpyx.bean.GoodChooseBean;
import com.jzbwlkj.zpyx.bean.GuideBean;
import com.jzbwlkj.zpyx.bean.HomeBannerBean;
import com.jzbwlkj.zpyx.bean.RecommendBean;
import com.jzbwlkj.zpyx.config.Config;
import com.jzbwlkj.zpyx.config.HttpArray;
import com.jzbwlkj.zpyx.config.HttpResult;
import com.jzbwlkj.zpyx.retrofit.RetrofitClient;
import com.jzbwlkj.zpyx.rxjava.BaseListObserver;
import com.jzbwlkj.zpyx.rxjava.BaseObjObserver;
import com.jzbwlkj.zpyx.rxjava.RxUtils;
import com.jzbwlkj.zpyx.util.GlideUtils;
import com.jzbwlkj.zpyx.util.SharePreUtil;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class DodgerActivity extends BaseActivity {

    @BindView(R.id.img_doder)
    ImageView imgDoder;
    @BindView(R.id.activity_dodger)
    RelativeLayout activityDodger;
    private SharePreUtil shareUtil;
    private boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodger);
        ButterKnife.bind(this);
    }

    @Override
    protected void init() {
        shareUtil = new SharePreUtil("user");
    }

    @Override
    protected void initData() {
        delay();

    }

    @Override
    protected void setData() {
    }


    private void delay() {
        if (!TextUtils.isEmpty(shareUtil.get("token"))) {
            Config.TOKEN = shareUtil.get("token");
            Logger.d("token：" + Config.TOKEN);
            maps.put("token",shareUtil.get("token"));
            loadBanner();
            loadGoodChoose(1);
            loadRecommend();
            isFirst = false;
        } else {
            isFirst = true;
        }

        Observable.just(1)
                .delay(3, TimeUnit.SECONDS)
                .compose(RxUtils.<Integer>io_main())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        toActivity();
                        finish();
                    }
                });
    }

    private void toActivity() {
        if (isFirst) {
            startActivity(new Intent(DodgerActivity.this, LoginActivity.class));
        } else {
            Intent mainIntent = new Intent(DodgerActivity.this, MainActivity.class);
            startActivity(mainIntent);
        }
    }

    private void loadBanner() {
        RetrofitClient.getInstance().createApi().banner(maps)
                .compose(RxUtils.<HttpArray<HomeBannerBean>>io_main())
                .subscribe(new BaseListObserver<HomeBannerBean>(false) {
                    @Override
                    protected void onHandleSuccess(List<HomeBannerBean> list) {
                        if (list != null || list.size() > 0) {
                            App.bannerList.clear();
                            for (int i = 0; i < list.size(); i++) {
                                App.bannerList.add(list.get(i).getAd_pic());
                            }
                        }
                    }
                });
    }

    private void loadRecommend() {
        RetrofitClient.getInstance().createApi().recommend(maps)
                .compose(RxUtils.<HttpArray<RecommendBean>>io_main())
                .subscribe(new BaseListObserver<RecommendBean>(false) {
                    @Override
                    protected void onHandleSuccess(List<RecommendBean> list) {
                        if (list != null || list.size() > 0) {
                            App.recommenflist.clear();
                            App.recommenflist.addAll(list);
                        }
                    }
                });
    }

    private void loadGoodChoose(final int page) {
        maps.put("page", page + "");
        RetrofitClient.getInstance().createApi()
                .goodChoose(maps)
                .compose(RxUtils.<HttpArray<GoodChooseBean>>io_main())
                .subscribe(new BaseListObserver<GoodChooseBean>(false) {
                    @Override
                    protected void onHandleSuccess(List<GoodChooseBean> list) {
                        if (list != null || list.size() > 0) {
                            App.goodsList.clear();
                            App.goodsList.addAll(list);
                        }
                    }
                });
    }

}
