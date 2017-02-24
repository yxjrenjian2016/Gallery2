package com.app.network;

import android.util.Log;

import com.app.model.BaseModel;
import com.app.ui.AppContext;
import com.app.utils.Utils;

import java.io.File;
import java.io.IOException;

import java.util.concurrent.TimeUnit;


import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 */
public enum Network {

    INSTANCE;

    public Api mApi;

    /**
     * 请求拦截器
     */
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request(); //Current Request
            Response originalResponse = chain.proceed(originalRequest);
            System.out.println(String.format("Sending request %s with headers %s ", originalRequest.url(), originalRequest.headers()));
            System.out.println(String.format("Got response HTTP %s %s \n\n with headers %s ", originalResponse.code(), originalResponse.message(),originalResponse.headers()));
            if (Utils.isNetworkConnectd()) {
                int maxAge = 60 * 5; // 在线缓存在5分钟内可读取

                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 7; // 离线缓存保存1周
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    //缓存路径和大小（10M）
    private static Cache NET_CACHE = new Cache(new File(AppContext.getAppApplication().getExternalCacheDir(),"netcache"), 10 * 1024 * 1024);

    private static OkHttpClient sClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .cache(NET_CACHE)
            .connectTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    public Api getApiService() {
        if (mApi == null) {
            mApi = new Retrofit.Builder()
                    .baseUrl("http://gank.io")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(sClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(Api.class);

        }
        return mApi;
    }

    public static <T> Observable.Transformer<BaseModel<T>,T> getResult() {

        return new Observable.Transformer<BaseModel<T>, T>() {

            @Override
            public Observable<T> call(Observable<BaseModel<T>> baseModelObservable) {
                return baseModelObservable.flatMap(new Func1<BaseModel<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(final BaseModel<T> tBaseModel) {
                        if( !tBaseModel.error){
                            return Observable.create(new Observable.OnSubscribe<T>() {
                                @Override
                                public void call(Subscriber<? super T> subscriber) {
                                    subscriber.onNext(tBaseModel.getResults());
                                    subscriber.onCompleted();
                                }
                            });
                        }
                        Log.v("net","error+"+tBaseModel.getResults().toString());
                        return Observable.error(new Throwable(tBaseModel.getResults().toString()));
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
