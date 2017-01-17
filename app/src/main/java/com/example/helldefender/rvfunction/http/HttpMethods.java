package com.example.helldefender.rvfunction.http;

import com.example.helldefender.rvfunction.app.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Helldefender on 2016/10/27.
 */

public class HttpMethods {
    private HttpApi httpApi;
    private Retrofit retrofit;
    private static final int DEFAUTL_TIMEOUT = 10;
    public static final String BASE_URL = "http://news-at.zhihu.com/api/4/";

    private HttpMethods() {
        File cacheFile = new File(MyApplication.getInstance().getCacheDir(), "Cache");
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(cacheFile, cacheSize);

//        new OkHttpClient.Builder();
//        new OkHttpClient().newBuilder();

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAUTL_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache)
                //应用拦截器，实现离线缓存
                .addInterceptor(new LogInterceptor())
                .addInterceptor(new HttpCacheInterceptor())
                //网络拦截器，实现在线缓存
                //.addNetworkInterceptor(new HttpCacheInterceptor())
                //.addNetworkInterceptor(new LogInterceptor())
                //OkHttpClient单例化，提供唯一的缓存文件访问入口
                .build();

        //应用和网络拦截器的优缺点：
        //疑问在于：添加header添加时在服务器返回请求数据后再对response进行header的添加 Cache-Control是如何起作用的
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        httpApi = retrofit.create(HttpApi.class);
    }

    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getNewSplash(Subscriber subscriber) {
        toSubscribe(httpApi.getNewsSplash(), subscriber);
    }

    public void getNewsLatest(Subscriber subscriber) {
        toSubscribe(httpApi.getNewsLatest(), subscriber);
    }

    public void getNewsBefore(Subscriber subscriber, int newsId) {
        toSubscribe(httpApi.getNewsBefore(newsId), subscriber);
    }

    public void getNewsContent(Subscriber subscriber, int newsId) {
        toSubscribe(httpApi.getNewsContent(newsId), subscriber);
    }

    public Observable getNewsContentObservable(int newsId) {
        return httpApi.getNewsContent(newsId);
    }

    public void getNewsExtra(Subscriber subscriber, int newsId) {
        toSubscribe(httpApi.getNewsExtra(newsId), subscriber);
    }

    public void getNewsLongComments(Subscriber subscriber, int newsId) {
        toSubscribe(httpApi.getNewsLongComments(newsId), subscriber);
    }

    public void getNewsShortComments(Subscriber subscriber, int newsId) {
        toSubscribe(httpApi.getNewsShortComments(newsId), subscriber);
    }

    public void getNewsThemes(Subscriber subscriber, int newsId) {
        toSubscribe(httpApi.getNewsThemes(newsId), subscriber);
    }

//    public <T> Observable<T> getNewsFactory(Class<T> eventType) {
//        return ;
//    }

    private void toSubscribe(Observable observable, Subscriber subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


}
