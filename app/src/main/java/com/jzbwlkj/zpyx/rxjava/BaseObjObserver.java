package com.jzbwlkj.zpyx.rxjava;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.jzbwlkj.zpyx.App;
import com.jzbwlkj.zpyx.config.HttpResult;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by admin on 2017/3/27.
 */

public abstract class BaseObjObserver<T> implements Observer<HttpResult<T>> {

    private static final String TAG = "BaseObjObserver";
    private Context mContext;
    private ProgressDialog progressDialog;

    protected BaseObjObserver() {

    }

    protected BaseObjObserver(Context context) {
        this.mContext = context.getApplicationContext();
        mContext = context;
    }

    protected BaseObjObserver(Context context, String message) {
        this.mContext = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.show();
    }


    @Override
    public void onNext(HttpResult<T> value) {
        if (value.status == 1) {
            T t = value.data;
            onHandleSuccess(t);
        } else {
            onHandleError(value.status, value.msg);
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "error:" + e.toString());
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected abstract void onHandleSuccess(T t);

    protected void onHandleError(int code, String msg) {
        //根据code处理
        Toast.makeText(App.app, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSubscribe(Disposable d) {

    }
}

