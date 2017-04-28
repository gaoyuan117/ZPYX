package com.jzbwlkj.zpyx.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jzbwlkj.zpyx.R;
import com.jzbwlkj.zpyx.base.BaseActivity;
import com.jzbwlkj.zpyx.frament.HomeFragment;
import com.jzbwlkj.zpyx.frament.MyFragment;
import com.jzbwlkj.zpyx.frament.DiscoverFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rb_main_home)
    RadioButton mHome;
    @BindView(R.id.rb_main_ng)
    RadioButton mNg;
    @BindView(R.id.rb_main_me)
    RadioButton mMe;
    @BindView(R.id.rg_main_navigation)
    RadioGroup mRadiogroup;
    @BindView(R.id.ll_main_replace)
    LinearLayout llMainReplace;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    public static MainActivity activity;

    private List<Fragment> mList;
    private List<RadioButton> mRadioButtos;
    private int count, cuuret, target;//RadioButton的个数,当前位置，目标位置
    private long firstTime = 0;
    private HomeFragment homeFragment;
    private DiscoverFragment navigationFragment;
    private MyFragment myFragment;
    private NotificationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void init() {
        activity = this;
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mList = new ArrayList<>();
        mRadioButtos = new ArrayList<>();
    }

    @Override
    protected void initData() {
        homeFragment = new HomeFragment();
        navigationFragment = new DiscoverFragment();
        myFragment = new MyFragment();
        mList.add(homeFragment);
        mList.add(navigationFragment);
        mList.add(myFragment);

        count = mRadiogroup.getChildCount();
        for (int i = 0; i < count; i++) {
            mRadioButtos.add((RadioButton) mRadiogroup.getChildAt(i));
        }
        showFirstFragment();

    }

    @Override
    protected void setData() {
        mRadiogroup.setOnCheckedChangeListener(this);

    }

    /**
     * RadioGroup点击事件
     *
     * @param radioGroup
     * @param i
     */
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        for (int i1 = 0; i1 < mRadioButtos.size(); i1++) {
            if (mRadioButtos.get(i1).getId() == i) {
                target = i1;
                break;
            }
        }
        chooseFragment();
    }

    /**
     * 判断显示Fragment
     */
    private void chooseFragment() {
        if (cuuret == target) {
            return;
        }
        Fragment currentFragment = mList.get(cuuret);
        Fragment targetFragment = mList.get(target);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (targetFragment.isAdded()) {
            fragmentTransaction.hide(currentFragment).show(targetFragment);
        } else {
            fragmentTransaction.hide(currentFragment).add(R.id.ll_main_replace, targetFragment);
        }
        fragmentTransaction.commit();
        cuuret = target;
    }


    /**
     * 显示第一个fragment
     */
    private void showFirstFragment() {
        mRadioButtos.get(0).setChecked(true);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.ll_main_replace, mList.get(0));
        transaction.commit();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {//如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {//两次按键小于2秒时，退出应用
//                    LoginActivity.loginActivity.finish();
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
