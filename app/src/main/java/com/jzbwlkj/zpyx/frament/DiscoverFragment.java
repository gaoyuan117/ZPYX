package com.jzbwlkj.zpyx.frament;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jzbwlkj.zpyx.R;
import com.jzbwlkj.zpyx.activity.WebViewActivity;
import com.jzbwlkj.zpyx.adapter.DiscoverAdapter;
import com.jzbwlkj.zpyx.base.BaseFragment;
import com.jzbwlkj.zpyx.bean.DiscoverAdBean;
import com.jzbwlkj.zpyx.bean.DiscoverBean;
import com.jzbwlkj.zpyx.config.HttpArray;
import com.jzbwlkj.zpyx.retrofit.RetrofitClient;
import com.jzbwlkj.zpyx.rxjava.BaseListObserver;
import com.jzbwlkj.zpyx.rxjava.RxUtils;
import com.jzbwlkj.zpyx.util.MyImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by admin on 2017/4/11.
 */

public class DiscoverFragment extends BaseFragment implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, OnBannerClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rv_discover)
    RecyclerView mRecyclerView;
    @BindView(R.id.img_discover_ss)
    ImageView mSearch;
    @BindView(R.id.sr_discover)
    SwipeRefreshLayout refreshLayout;

    private View headView;
    private Banner mBanner;
    private DiscoverAdapter mAdapter;
    private List<DiscoverBean> mList;
    private List<String> bannerList;
    private List<DiscoverAdBean> bannerBeanList;
    private int page = 1;

    @Override
    protected View loadLayout(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_discover, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init(View view) {
        headView = View.inflate(getActivity(), R.layout.head_discover, null);
        mBanner = (Banner) headView.findViewById(R.id.banner_discover);
        mList = new ArrayList<>();
        bannerList = new ArrayList<>();
        bannerBeanList = new ArrayList<>();
        mAdapter = new DiscoverAdapter(R.layout.item_discover, mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.addHeaderView(headView);
    }

    @Override
    protected void initData() {
        loadDiscover(1);
        loadDiscoverAd();
    }

    @Override
    protected void set() {
        mSearch.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        mBanner.setOnBannerClickListener(this);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
    }

    private void loadDiscover(final int page) {
        maps.put("page", page + "");
        RetrofitClient.getInstance().createApi().discoverFind(maps)
                .compose(RxUtils.<HttpArray<DiscoverBean>>io_main())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        refreshLayout.setRefreshing(false);
                    }
                })
                .subscribe(new BaseListObserver<DiscoverBean>(mAdapter) {
                    @Override
                    protected void onHandleSuccess(List<DiscoverBean> list) {
                        refreshLayout.setRefreshing(false);
                        if(page==1){
                            mList.clear();
                        }
                        mAdapter.loadMoreComplete();
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void loadDiscoverAd() {
        RetrofitClient.getInstance().createApi().discoverBanner(maps)
                .compose(RxUtils.<HttpArray<DiscoverAdBean>>io_main())
                .subscribe(new BaseListObserver<DiscoverAdBean>() {
                    @Override
                    protected void onHandleSuccess(List<DiscoverAdBean> list) {
                        bannerList.clear();
                        bannerBeanList.clear();
                        bannerBeanList.addAll(list);
                        for (int i = 0; i < list.size(); i++) {
                            bannerList.add(list.get(i).getAd_pic());
                        }
                        setBanner(bannerList);
                    }
                });

    }

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
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_discover_ss:
                toActivity("search/search.html");
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        toActivity("log/detail.html?id=" + mList.get(position).getLog_id());
    }

    @Override
    public void onRefresh() {
        page = 1;
        loadDiscover(page);
        loadDiscoverAd();
    }

    @Override
    public void OnBannerClick(int position) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("url", bannerBeanList.get(position - 1).getAd_url());
        startActivity(intent);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        loadDiscover(page);
    }
}
