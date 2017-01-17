package com.example.helldefender.rvfunction.http;

import com.example.helldefender.rvfunction.entity.TEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

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
    private HttpService httpService;
    private Retrofit retrofit;
    private static final int DEFAUTL_TIMEOUT = 5;
    public static final String BASE_URL = "http://news-at.zhihu.com/api/4/";

    private HttpMethods() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAUTL_TIMEOUT, TimeUnit.SECONDS);
        //手动创建一个client并指定超时  okhttpclient
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                // retrofit默认将相应体转换为okhttp中的responsebody,call中设置的泛型类型为自定义类型reposenseinfo
                //将json格式的数据转化为java bean 格式的数据 retrofit需要依赖另一个库
                //自定义gson对象[需要调整json里的格式]adddconverterfactory.gsonconverterfactory.create(gson)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getHttpInfo(Subscriber<TEntity> progressSubscriber, int id, int TAG) {
        //不同的参数 不同的url httpInfo不同的返回值 observable httpservice
        Observable observable=null;
        if (TAG == 1) {
            if (id == 101) {
                observable = httpService.getHttpSplashInfo();
            } else if (id == 102) {
                observable = httpService.getHttpLatestNewsInfo();
            }
        } else if (TAG == 2) {
            observable = httpService.getHttpNewsContentInfo(id);
        } else if (TAG == 3) {
            observable = httpService.getHttpNewsBeforeInfo(id);
        } else if (TAG == 4) {
            observable = httpService.getHttpNewsOtherInfo(id);
        } else if (TAG == 5) {
            observable = httpService.getHttpNewsLongCommentsInfo(id);
        } else if (TAG == 6) {
            observable=httpService.getHttpNewsShortCommentsInfo(id);
        } else if (TAG == 7) {
            observable = httpService.getHttpThemeContentInfo(id);
        }
        toSubscribe(observable, progressSubscriber);
    }

    private void toSubscribe(Observable observable, Subscriber subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

}
