package com.jzbwlkj.zpyx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jzbwlkj.zpyx.App;
import com.jzbwlkj.zpyx.R;
import com.jzbwlkj.zpyx.base.BaseActivity;
import com.jzbwlkj.zpyx.bean.CommonBean;
import com.jzbwlkj.zpyx.bean.LoginBean;
import com.jzbwlkj.zpyx.config.Config;
import com.jzbwlkj.zpyx.config.HttpResult;
import com.jzbwlkj.zpyx.retrofit.RetrofitClient;
import com.jzbwlkj.zpyx.rxjava.BaseObjObserver;
import com.jzbwlkj.zpyx.rxjava.RxUtils;
import com.jzbwlkj.zpyx.util.CountDownUtils;
import com.jzbwlkj.zpyx.util.GlideUtils;
import com.jzbwlkj.zpyx.util.SharePreUtil;
import com.jzbwlkj.zpyx.util.Util;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends BaseActivity implements CountDownUtils.CountdownListener {
    public static LoginActivity loginActivity;

    @BindView(R.id.et_login_phone)
    EditText mPhone;
    @BindView(R.id.et_login_code)
    EditText mCode;
    @BindView(R.id.tv_login_getcode)
    TextView mGetcode;
    @BindView(R.id.tv_login)
    TextView mLogin;
    @BindView(R.id.img_login_avatar)
    CircleImageView avatar;
    @BindView(R.id.img_login_sex)
    ImageView sex;
    @BindView(R.id.tv_login_name)
    TextView name;
    @BindView(R.id.img_login_back)
    ImageView back;

    private CountDownUtils countDown;
    private String phone, code;
    private SharePreUtil shareUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void init() {
        loginActivity = this;
        countDown = new CountDownUtils(mGetcode, "%s秒后重新获取", 60);
        shareUtil = new SharePreUtil("user");

        if (shareUtil.get("sex").equals("0")) {
            sex.setImageResource(R.mipmap.xingbie);
        } else {
            sex.setImageResource(R.mipmap.nv);
        }
        name.setText(shareUtil.get("name"));
        Glide.with(this).load(Config.BASE_URL+shareUtil.get("avatar")).error(R.mipmap.logo2).into(avatar);
        if (!TextUtils.isEmpty(shareUtil.get("token"))) {
            Config.TOKEN = shareUtil.get("token");
            Logger.d("token：" + Config.TOKEN);
            mPhone.setText(shareUtil.get("phone"));
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setData() {
        countDown.setCountdownListener(this);
    }

    @OnClick({R.id.tv_login_getcode, R.id.tv_login,R.id.img_login_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_getcode:
                phone = mPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Util.toast("请输入手机号");
                    return;
                }
                countDown.start();
                getCode();
                break;
            case R.id.tv_login:
                code = mCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Util.toast("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    Util.toast("请输入手机号");
                    return;
                }
                login();
                break;
            case R.id.img_login_back:
                finish();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        maps.put("phone", phone);
        RetrofitClient.getInstance().createApi().getCode(maps)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, "发送中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        Util.toast("验证码发送成功");
                    }
                });
    }

    /**
     * 登录
     */
    private void login() {
        maps.put("user_phone", phone);
        maps.put("verify_code", code);
        RetrofitClient.getInstance().createApi().login(maps)
                .compose(RxUtils.<HttpResult<LoginBean>>io_main())
                .subscribe(new BaseObjObserver<LoginBean>(this, "登录中") {
                    @Override
                    protected void onHandleSuccess(LoginBean loginBean) {
                        Config.TOKEN = loginBean.getUser_token();
                        Logger.d("token", Config.TOKEN);
                        saveInfo(loginBean);
                        startActivity(new Intent(App.app, MainActivity.class));
                        finish();
                    }
                });
    }

    private void saveInfo(LoginBean bean) {
        shareUtil.edit()
                .putString("token", bean.getUser_token())
                .putString("phone", phone)
                .putString("name", bean.getUser_nickname())
                .putString("avatar", bean.getUser_portrait())
                .putString("sex", bean.getUser_sex() + "")
                .putString("uid",bean.getUser_id()+"")
                .commit();
    }

    @Override
    public void onStartCount() {
        mGetcode.setEnabled(false);
    }

    @Override
    public void onFinishCount() {
        mGetcode.setEnabled(true);
        mGetcode.setText("重新获取验证码");
    }

    @Override
    public void onUpdateCount(int currentRemainingSeconds) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDown.isRunning()) {
            countDown.stop();
        }
    }
}
