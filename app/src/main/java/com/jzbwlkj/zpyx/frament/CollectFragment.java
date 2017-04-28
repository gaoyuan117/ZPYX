package com.jzbwlkj.zpyx.frament;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jzbwlkj.zpyx.R;
import com.jzbwlkj.zpyx.adapter.DiscoverAdapter;
import com.jzbwlkj.zpyx.base.BaseFragment;
import com.jzbwlkj.zpyx.bean.DiscoverBean;
import com.jzbwlkj.zpyx.config.HttpArray;
import com.jzbwlkj.zpyx.retrofit.RetrofitClient;
import com.jzbwlkj.zpyx.rxjava.BaseListObserver;
import com.jzbwlkj.zpyx.rxjava.RxUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/4/12.
 */

public class CollectFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.rv_my_collect)
    RecyclerView mRecyclerView;

    private DiscoverAdapter mAdapter;
    private List<DiscoverBean> mList;
    private int page = 1;

    @Override
    protected View loadLayout(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_collect, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init(View view) {
        mList = new ArrayList<>();
        mAdapter = new DiscoverAdapter(R.layout.item_discover, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        getCollect();
    }

    @Override
    protected void set() {
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCollect();
            }
        }, mRecyclerView);

        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getCollect();
        }
    }

    public void getCollect() {
        maps.put("type", "0");
        maps.put("page", page + "");
        RetrofitClient.getInstance().createApi()
                .getRzCollect(maps)
                .compose(RxUtils.<HttpArray<DiscoverBean>>io_main())
                .subscribe(new BaseListObserver<DiscoverBean>(false) {
                    @Override
                    protected void onHandleSuccess(List<DiscoverBean> list) {
                        mList.clear();
                        if (list != null) {
                            mList.addAll(list);
                            mAdapter.setEnableLoadMore(false);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        toActivity("log/detail.html?id="+mList.get(position).getLog_id());
    }
}
