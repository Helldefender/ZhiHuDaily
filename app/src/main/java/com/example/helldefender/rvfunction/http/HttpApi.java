package com.example.helldefender.rvfunction.http;

import com.example.helldefender.rvfunction.entity.NewsBean;
import com.example.helldefender.rvfunction.entity.NewsCommentsBean;
import com.example.helldefender.rvfunction.entity.NewsContentBean;
import com.example.helldefender.rvfunction.entity.NewsExtraBean;
import com.example.helldefender.rvfunction.entity.SplashBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Helldefender on 2016/10/27.
 */

public interface HttpApi {
    @GET("start-image/1080*1776")
    Observable<SplashBean> getNewsSplash();

    @GET("news/latest")
    Observable<NewsBean> getNewsLatest();

    @GET("news/{id}")
    Observable<NewsContentBean> getNewsContent(@Path("id") int id);

    @GET("news/before/{id}")
    Observable<NewsBean> getNewsBefore(@Path("id") int id);

    @GET("story-extra/{id}")
    Observable<NewsExtraBean> getNewsExtra(@Path("id") int id);

    @GET("story/{id}/long-comments")
    Observable<NewsCommentsBean> getNewsLongComments(@Path("id") int id);

    @GET("story/{id}/short-comments")
    Observable<NewsCommentsBean> getNewsShortComments(@Path("id") int id);

    @GET("theme/{id}")
    Observable<NewsBean> getNewsThemes(@Path("id") int id);
}
