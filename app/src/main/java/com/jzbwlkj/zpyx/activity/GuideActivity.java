package com.jzbwlkj.zpyx.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;
import com.jzbwlkj.zpyx.App;
import com.jzbwlkj.zpyx.R;
import com.jzbwlkj.zpyx.base.BaseActivity;
import com.jzbwlkj.zpyx.bean.GuideBean;
import com.jzbwlkj.zpyx.config.Config;
import com.jzbwlkj.zpyx.config.HttpResult;
import com.jzbwlkj.zpyx.retrofit.RetrofitClient;
import com.jzbwlkj.zpyx.rxjava.BaseObjObserver;
import com.jzbwlkj.zpyx.rxjava.RxUtils;
import com.jzbwlkj.zpyx.util.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuideActivity extends BaseActivity {

    @BindView(R.id.vp_guide_guide)
    ViewPager mViewPager;
    @BindView(R.id.img)
    ImageView activityGuide;


    private List<View> mList;
    private ImageView mStart;
    private SharedPreferences sp;
    private ViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        boolean isFirst = firstLogin();
        if (!isFirst) {//判断是否是第一次登陆
            startActivity(new Intent(this, DodgerActivity.class));
            finish();
            return;
        }
    }

    @Override
    protected void init() {
        mList = new ArrayList<>();
        mAdapter = new ViewAdapter(mList);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        getPic();
    }

    @Override
    protected void setData() {

    }

    private void getPic() {
        RetrofitClient.getInstance().createApi().guidePic(maps)
                .compose(RxUtils.<HttpResult<GuideBean>>io_main())
                .subscribe(new BaseObjObserver<GuideBean>() {
                    @Override
                    protected void onHandleSuccess(GuideBean guideBean) {
//                        for (int i = 0; i < 3; i++) {
//                            ImageView imageView = new ImageView(GuideActivity.this);
////                            Target<GlideDrawable> into = Glide.with(App.app).load(Config.BASE_URL + guideBean.getStartUp()).into(imageView);
//                            Bitmap bitmap = BitmapFactory.decodeFile(Config.BASE_URL + guideBean.getStartUp());
//                            imageView.setImageBitmap(bitmap);
//                            mList.add(imageView);
//                        }
                        Log.e("gy",Config.BASE_URL + guideBean.getStartUp());
                        Bitmap bitmap = BitmapFactory.decodeFile(Config.BASE_URL + guideBean.getStartUp());
//                        Glide.with(GuideActivity.this).load(Config.BASE_URL + guideBean.getStartUp()).
//                        activityGuide.setImageBitmap(bitmap);
//                        mAdapter.notifyDataSetChanged();
                    }
                });
    }


    //判断是否是第一次登陆
    public boolean firstLogin() {
        sp = getSharedPreferences("first_login", MODE_PRIVATE);
        if (sp.getString("first", null) != null) {
            return false;
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("first", "first");
            editor.commit();
            return true;
        }
    }
}
