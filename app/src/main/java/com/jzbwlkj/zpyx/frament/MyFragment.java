package com.jzbwlkj.zpyx.frament;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jzbwlkj.zpyx.R;
import com.jzbwlkj.zpyx.adapter.ViewPagerAdapter;
import com.jzbwlkj.zpyx.base.BaseFragment;
import com.jzbwlkj.zpyx.bean.UserInfoBean;
import com.jzbwlkj.zpyx.config.HttpResult;
import com.jzbwlkj.zpyx.retrofit.RetrofitClient;
import com.jzbwlkj.zpyx.rxjava.BaseObjObserver;
import com.jzbwlkj.zpyx.rxjava.RxBus;
import com.jzbwlkj.zpyx.rxjava.RxUtils;
import com.jzbwlkj.zpyx.util.GlideUtils;
import com.jzbwlkj.zpyx.util.SharePreUtil;
import com.jzbwlkj.zpyx.util.ShareUtils;
import com.jzbwlkj.zpyx.view.MyViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

/**
 * Created by admin on 2017/4/11.
 */

public class MyFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener, SwipeRefreshLayout.OnRefreshListener, PlatformActionListener {

    @BindView(R.id.img_my_set)
    ImageView mSet;
    @BindView(R.id.img_my_avatar)
    CircleImageView mAvatar;
    @BindView(R.id.tv_my_name)
    TextView mName;
    @BindView(R.id.tv_my_fbrz)
    TextView mFbrz;
    @BindView(R.id.tv_my_tjsp)
    TextView mTjsp;
    @BindView(R.id.tv_my_wdrz)
    TextView mWdrz;
    @BindView(R.id.tv_my_yqhy)
    TextView mYqhy;
    @BindView(R.id.tb_my)
    TabLayout mTabLayout;
    @BindView(R.id.vp_my)
    MyViewPager mViewPager;
    @BindView(R.id.img_my_qd)
    LinearLayout mQd;
    @BindView(R.id.img_my_jf)
    LinearLayout mJf;
    @BindView(R.id.img_my_sex)
    ImageView mSex;
    @BindView(R.id.tv_my_jf)
    TextView mTvJf;
    @BindView(R.id.sr_my)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragments;
    private List<String> titles;
    public SingleFragment singleFragment;
    public CollectFragment collectFragment;
    private String uid;

    @Override
    protected View loadLayout(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init(View view) {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        singleFragment = new SingleFragment();
        collectFragment = new CollectFragment();
        fragments.add(singleFragment);
        fragments.add(collectFragment);
        titles.add("收藏单品");
        titles.add("收藏日志");

        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(viewPagerAdapter);
        uid = new SharePreUtil("user").get("uid");
        Log.e("gy", "uid：" + uid);
    }

    @Override
    protected void initData() {
        getUserInfo();
    }

    @Override
    protected void set() {
        appBarLayout.addOnOffsetChangedListener(this);
        refreshLayout.setOnRefreshListener(this);

        RxBus.getInstance().subscribe(String.class, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                refreshLayout.setRefreshing(false);
            }
        });

        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(mTabLayout, 50, 50);
            }
        });
    }

    @OnClick({R.id.tv_my_fbrz, R.id.tv_my_tjsp, R.id.tv_my_wdrz, R.id.tv_my_yqhy, R.id.img_my_qd, R.id.img_my_jf, R.id.img_my_set, R.id.img_my_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_my_fbrz:
                toActivity("mine/log/add_log.html", "");
                break;
            case R.id.tv_my_tjsp:
                toActivity("mine/goods/add_goods.html", "");
                break;
            case R.id.tv_my_wdrz:
                toActivity("mine/log/index.html", "");
                break;
            case R.id.tv_my_yqhy:
                share(getActivity(), "http://zhaiwushuo.jzbwlkj.com/html/zhuce.html?userid=" + uid);
                break;
            case R.id.img_my_set:
                toActivity("mine/setting/index.html", "");
                break;
            case R.id.img_my_avatar:
                toActivity("mine/setting/heder_pic.html", "");
                break;
            case R.id.img_my_qd:
                toActivity("mine/sign.html", "");
                break;
            case R.id.img_my_jf:
                toActivity("shop/index.html", "");
                break;
        }
    }

    private void getUserInfo() {
        RetrofitClient.getInstance().createApi().getUserInfo(maps)
                .compose(RxUtils.<HttpResult<UserInfoBean>>io_main())
                .subscribe(new BaseObjObserver<UserInfoBean>() {
                    @Override
                    protected void onHandleSuccess(UserInfoBean userInfoBean) {
                        mTvJf.setText("积分：" + userInfoBean.getUser_integral() + "");
                        if (TextUtils.isEmpty(userInfoBean.getUser_nickname())) {
                            mName.setText(userInfoBean.getUser_phone());
                        } else {
                            mName.setText(userInfoBean.getUser_nickname());
                        }
                        GlideUtils.glideAvatar(userInfoBean.getUser_portrait(), mAvatar);
                        if (userInfoBean.getUser_sex() == 0) {//男
                            mSex.setImageResource(R.mipmap.xingbie);
                        } else {//女
                            mSex.setImageResource(R.mipmap.nv);
                        }
                    }
                });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        refreshLayout.setEnabled(verticalOffset >= 0 ? true : false);
    }

    @Override
    public void onRefresh() {
        getUserInfo();
        singleFragment.getCollect();
        collectFragment.getCollect();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getUserInfo();
            singleFragment.getCollect();
            collectFragment.getCollect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }


    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public void share(Context context, String url) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("买手大叔");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("买手大叔");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        oks.setImageUrl("http://zhaiwushuo.jzbwlkj.com/html/images/logo2.png"); // 必须有图片路径才能在微信分享有标题
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("买手大叔");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://zhaiwushuo.jzbwlkj.com/html/images/logo2.png");
        // 启动分享GUI
//        oks.setCallback(this);
        oks.show(context);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //TODO 调用接口
                Toast.makeText(mActivity, "分享成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
