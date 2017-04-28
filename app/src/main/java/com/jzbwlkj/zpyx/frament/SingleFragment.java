package com.jzbwlkj.zpyx.frament;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jzbwlkj.zpyx.R;
import com.jzbwlkj.zpyx.adapter.HomeAdapter;
import com.jzbwlkj.zpyx.base.BaseFragment;
import com.jzbwlkj.zpyx.bean.GoodChooseBean;
import com.jzbwlkj.zpyx.config.HttpArray;
import com.jzbwlkj.zpyx.retrofit.RetrofitClient;
import com.jzbwlkj.zpyx.rxjava.BaseListObserver;
import com.jzbwlkj.zpyx.rxjava.RxBus;
import com.jzbwlkj.zpyx.rxjava.RxUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Action;

/**
 * Created by admin on 2017/4/12.
 */

public class SingleFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv_my_single)
    RecyclerView mRecyclerView;
    private HomeAdapter homeAdapter;
    private List<GoodChooseBean> mList;
    private int page = 1;

    @Override
    protected View loadLayout(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_single, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init(View view) {
        mList = new ArrayList<>();
        homeAdapter = new HomeAdapter(R.layout.item_home, mList);
        mRecyclerView.setAdapter(homeAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initData() {
        getCollect();
    }

    @Override
    protected void set() {
        homeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCollect();
            }
        }, mRecyclerView);
        homeAdapter.setOnItemClickListener(this);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getCollect();
        }
    }

    public void getCollect() {
        maps.put("type", "1");
        maps.put("page", page + "");
        RetrofitClient.getInstance().createApi().getSingleCollect(maps)
                .compose(RxUtils.<HttpArray<GoodChooseBean>>io_main())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        RxBus.getInstance().send("1");
                    }
                })
                .subscribe(new BaseListObserver<GoodChooseBean>(false) {
                    @Override
                    protected void onHandleSuccess(List<GoodChooseBean> list) {
                        mList.clear();
                        mList.addAll(list);
                        homeAdapter.setEnableLoadMore(false);
                        homeAdapter.notifyDataSetChanged();
                        RxBus.getInstance().send("1");
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        toActivity("shop/goods_info.html?goods_id=" + mList.get(position).getGoods_id());
    }
}
