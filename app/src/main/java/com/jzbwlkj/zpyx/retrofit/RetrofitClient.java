package com.jzbwlkj.zpyx.retrofit;


import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.jzbwlkj.zpyx.config.Config;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by admin on 2017/3/27.
 */

public class RetrofitClient {

    private static final int DEFAULT_TIMEOUT = 20;
    private Api apiService;
    private static OkHttpClient okHttpClient;
    public static String baseUrl = Config.BASE_URL;
    private static Retrofit retrofit;


    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 自己填写Base Url
     *
     * @param url
     * @return
     */
    public static RetrofitClient getInstance(String url) {
        return new RetrofitClient(url);
    }

    /**
     * 添加请求头
     *
     * @param url
     * @param headers
     * @return
     */
    public static RetrofitClient getInstance(String url, Map<String, String> headers) {
        return new RetrofitClient(url, headers);
    }


    private RetrofitClient() {
        this(baseUrl, null);
    }

    private RetrofitClient(String url) {

        this(url, null);
    }

    private RetrofitClient(String url, Map<String, String> headers) {
        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new BaseInterceptor(headers))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    /**
     * create BaseApi  defalte ApiManager
     *
     * @return ApiManager
     */
    public Api createApi() {
        apiService = create(Api.class);
        return apiService;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

}