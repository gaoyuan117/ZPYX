package com.jzbwlkj.zpyx.rxjava;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jzbwlkj.zpyx.App;
import com.jzbwlkj.zpyx.adapter.DiscoverAdapter;
import com.jzbwlkj.zpyx.adapter.HomeAdapter;
import com.jzbwlkj.zpyx.config.CommonDialog;
import com.jzbwlkj.zpyx.config.HttpArray;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by admin on 2017/3/27.
 */

public abstract class BaseListObserver<T> implements Observer<HttpArray<T>> {
    private static final String TAG = "BaseObjObserver";
    private Context mContext;
    private ProgressDialog progressDialog;
    private boolean isToast = true;
    private BaseQuickAdapter adapter;

    protected BaseListObserver() {

    }
    protected BaseListObserver(BaseQuickAdapter adapter) {
        this.adapter = adapter;
    }
    protected BaseListObserver(BaseQuickAdapter adapter,boolean isToast) {
        this.adapter = adapter;
        this.isToast = isToast;
    }

    protected BaseListObserver(boolean isToast) {
        this.isToast = isToast;
    }

    protected BaseListObserver(Context context) {
        mContext = context;
    }

    protected BaseListObserver(Context context, String message) {
        this.mContext = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void onNext(HttpArray<T> value) {
        if (value.status == 1) {
            List<T> t = value.data;
            onHandleSuccess(t);
        } else {
            onHandleError(value.status,value.msg);
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "error:" + e.toString());
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
        if(adapter!=null){
            adapter.loadMoreFail();
        }
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete");
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    protected abstract void onHandleSuccess(List<T> list);

    protected void onHandleError(int code,String msg) {
        //根据code处理
        if(code==-94){
            CommonDialog.loginDialog(mContext);
        }
        if(adapter!=null){
            if(code==0){
                adapter.loadMoreEnd();
            }
        }
        if(isToast){
            if(adapter instanceof HomeAdapter){
                Toast.makeText(App.app, "没有更多优选商品了!", Toast.LENGTH_SHORT).show();
            }else if(adapter instanceof DiscoverAdapter){
                Toast.makeText(App.app, "没有更多日志了", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(App.app, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

}
