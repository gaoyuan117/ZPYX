package com.jzbwlkj.zpyx.frament;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jzbwlkj.zpyx.App;
import com.jzbwlkj.zpyx.R;
import com.jzbwlkj.zpyx.activity.WebViewActivity;
import com.jzbwlkj.zpyx.adapter.HomeAdapter;
import com.jzbwlkj.zpyx.base.BaseFragment;
import com.jzbwlkj.zpyx.bean.GoodChooseBean;
import com.jzbwlkj.zpyx.bean.HomeBannerBean;
import com.jzbwlkj.zpyx.bean.RecommendBean;
import com.jzbwlkj.zpyx.config.HttpArray;
import com.jzbwlkj.zpyx.config.HttpResult;
import com.jzbwlkj.zpyx.retrofit.RetrofitClient;
import com.jzbwlkj.zpyx.rxjava.BaseListObserver;
import com.jzbwlkj.zpyx.rxjava.RxUtils;
import com.jzbwlkj.zpyx.util.GlideUtils;
import com.jzbwlkj.zpyx.util.MyImageLoader;
import com.jzbwlkj.zpyx.view.RoundImageView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by admin on 2017/4/11.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener, OnBannerClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rl_home_ss)
    RelativeLayout mSearch;
    @BindView(R.id.img_home_xiaoxi)
    ImageView mXiaoxi;
    @BindView(R.id.rv_home)
    RecyclerView mRecyclerView;
    @BindView(R.id.sr_home)
    SwipeRefreshLayout refreshLayout;

    LinearLayout llTj, ll1, ll2, ll3;
    Banner mBanner;
    RoundImageView headImg1;
    TextView headTitle1;
    TextView headMoney1;
    RoundImageView headImg2;
    TextView headTitle2;
    TextView headMoney2;
    RoundImageView headImg3;
    TextView headTitle3;
    TextView headMoney3;
    TextView tvTuiJian;
    ImageView tj;

    private HomeAdapter mAdapter;
    private List<GoodChooseBean> mList;
    private List<String> bannerList;
    private List<RecommendBean> recommendList;
    private List<HomeBannerBean> bannerBeenList;
    private View headView;
    private int page = 1;

    @Override
    protected View loadLayout(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init(View view) {
        headView = View.inflate(getActivity(), R.layout.head_home, null);
        mList = new ArrayList<>();
        if (App.goodsList.size() > 0) {
            mList.addAll(App.goodsList);
        }

        bannerList = new ArrayList<>();
        bannerBeenList = new ArrayList<>();
        initHeadView();
        mAdapter = new HomeAdapter(R.layout.item_home, mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.addHeaderView(headView);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);

        if (App.recommenflist.size() > 0) {
            initRecommend(App.recommenflist);
        }
        if (App.bannerList.size() > 0) {
            setBanner(App.bannerList);
        }
    }

    @Override
    protected void initData() {
        loadBanner();
        loadRecommend();
        loadGoodChoose(page);
        hasNews();
    }

    @Override
    protected void set() {
        mXiaoxi.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        ll1.setOnClickListener(this);
        ll2.setOnClickListener(this);
        ll3.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mBanner.setOnBannerClickListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    /**
     * 初始化头布局
     */
    private void initHeadView() {
        mBanner = (Banner) headView.findViewById(R.id.banner);

        tj = (ImageView) headView.findViewById(R.id.img_tj);
        headImg1 = (RoundImageView) headView.findViewById(R.id.img1_home_tj);
        headImg2 = (RoundImageView) headView.findViewById(R.id.img2_home_tj);
        headImg3 = (RoundImageView) headView.findViewById(R.id.img3_home_tj);

        headTitle1 = (TextView) headView.findViewById(R.id.tv1_home_title);
        headTitle2 = (TextView) headView.findViewById(R.id.tv2_home_title);
        headTitle3 = (TextView) headView.findViewById(R.id.tv3_home_title);

        headMoney1 = (TextView) headView.findViewById(R.id.tv1_home_money);
        headMoney2 = (TextView) headView.findViewById(R.id.tv2_home_money);
        headMoney3 = (TextView) headView.findViewById(R.id.tv3_home_money);

        ll1 = (LinearLayout) headView.findViewById(R.id.ll1_home);
        ll2 = (LinearLayout) headView.findViewById(R.id.ll2_home);
        ll3 = (LinearLayout) headView.findViewById(R.id.ll3_home);

        tvTuiJian = (TextView) headView.findViewById(R.id.tv_home_tuijian);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "text_type.OTF");
        tvTuiJian.setTypeface(typeface);
    }

    private void loadBanner() {
        RetrofitClient.getInstance().createApi().banner(maps)
                .compose(RxUtils.<HttpArray<HomeBannerBean>>io_main())
                .subscribe(new BaseListObserver<HomeBannerBean>() {
                    @Override
                    protected void onHandleSuccess(List<HomeBannerBean> list) {
                        bannerBeenList.clear();
                        bannerList.clear();
                        bannerBeenList.addAll(list);
                        for (int i = 0; i < list.size(); i++) {
                            bannerList.add(list.get(i).getAd_pic());
                        }
                        setBanner(bannerList);
                    }
                });
    }

    private void loadRecommend() {
        RetrofitClient.getInstance().createApi().recommend(maps)
                .compose(RxUtils.<HttpArray<RecommendBean>>io_main())
                .subscribe(new BaseListObserver<RecommendBean>() {
                    @Override
                    protected void onHandleSuccess(List<RecommendBean> list) {
                        recommendList = list;
                        initRecommend(list);
                    }
                });
    }

    private void initRecommend(List<RecommendBean> list) {
        if (list.size() == 0) {
            tj.setVisibility(View.GONE);
            llTj.setVisibility(View.GONE);
        } else if (list.size() == 1) {
            GlideUtils.glide(list.get(0).getGoods_pic(), headImg1);
            headTitle1.setText(list.get(0).getGoods_name());
            headMoney1.setText("￥ " + list.get(0).getGoods_price());
        } else if (list.size() == 2) {
            GlideUtils.glide(list.get(0).getGoods_pic(), headImg1);
            headTitle1.setText(list.get(0).getGoods_name());
            headMoney1.setText("￥ " + list.get(0).getGoods_price());
            GlideUtils.glide(list.get(1).getGoods_pic(), headImg2);
            headTitle2.setText(list.get(1).getGoods_name());
            headMoney2.setText("￥ " + list.get(1).getGoods_price());
        } else if (list.size() == 3) {
            GlideUtils.glide(list.get(0).getGoods_pic(), headImg1);
            headTitle1.setText(list.get(0).getGoods_name());
            headMoney1.setText("￥ " + list.get(0).getGoods_price());
            GlideUtils.glide(list.get(1).getGoods_pic(), headImg2);
            headTitle2.setText(list.get(1).getGoods_name());
            headMoney2.setText("￥ " + list.get(1).getGoods_price());
            GlideUtils.glide(list.get(2).getGoods_pic(), headImg3);
            headTitle3.setText(list.get(2).getGoods_name());
            headMoney3.setText("￥ " + list.get(2).getGoods_price());
        }
    }

    private void loadGoodChoose(final int page) {
        maps.put("page", page + "");
        RetrofitClient.getInstance().createApi()
                .goodChoose(maps)
                .compose(RxUtils.<HttpArray<GoodChooseBean>>io_main())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        refreshLayout.setRefreshing(false);
                        mAdapter.disableLoadMoreIfNotFullPage();
                    }
                })
                .subscribe(new BaseListObserver<GoodChooseBean>(mAdapter) {
                    @Override
                    protected void onHandleSuccess(List<GoodChooseBean> list) {
                        if (page == 1) {
                            mList.clear();
                        }
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mAdapter.loadMoreComplete();
                    }
                });
    }

    private void hasNews() {
        RetrofitClient.getInstance().createApi().hasNews(maps)
                .compose(RxUtils.<HttpResult>io_main())
                .subscribe(new Consumer<HttpResult>() {
                    @Override
                    public void accept(HttpResult httpResult) throws Exception {
                        if (httpResult.status == 1) {//有消息
                            mXiaoxi.setImageResource(R.mipmap.youxiaoxi);
                        } else {
                            mXiaoxi.setImageResource(R.mipmap.xiaoxi);
                        }
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            hasNews();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hasNews();
    }

    /**
     * set Banner
     */
    private void setBanner(List<String> list) {
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new MyImageLoader());
        //设置图片集合
        mBanner.setImages(list);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        mBanner.setViewPagerIsScroll(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_home_xiaoxi:
                toActivity("mine/message/index.html", "");
                break;
            case R.id.rl_home_ss:
                toActivity("search/search.html", "");
                break;
            case R.id.ll1_home:
                if (recommendList.size() >= 0) {
                    toActivity("shop/goods_info.html?goods_id=" + recommendList.get(0).getGoods_id(), recommendList.get(0).getGoods_id() + "");
                }
                break;
            case R.id.ll2_home:
                if (recommendList.size() > 1) {
                    toActivity("shop/goods_info.html?goods_id=" + recommendList.get(1).getGoods_id(), recommendList.get(1).getGoods_id() + "");
                }
                break;
            case R.id.ll3_home:
                if (recommendList.size() >= 2) {
                    toActivity("shop/goods_info.html?goods_id=" + recommendList.get(2).getGoods_id(), recommendList.get(2).getGoods_id() + "");
                }
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        toActivity("shop/goods_info.html?goods_id=" + mList.get(position).getGoods_id(), mList.get(position).getGoods_id() + "");
    }

    @Override
    public void OnBannerClick(int position) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("url", bannerBeenList.get(position - 1).getAd_url());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        page = 1;
        hasNews();
        loadBanner();
        loadRecommend();
        loadGoodChoose(page);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        loadGoodChoose(page);
    }
}
