package com.example.helldefender.rvfunction.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.helldefender.rvfunction.app.MyApplication;
import com.example.helldefender.rvfunction.data.entity.NewsContentBean;

/**
 * Created by Helldefender on 2016/10/19.
 */

public class DisplayUtil {
    public static String structHtml(NewsContentBean newsContentBean) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div class=\"img-wrap\">").append("<h1 class=\"headline-title\">")
                .append(newsContentBean.getTitle()).append("</h1>")
                .append("<span class=\"img-source\">")
                .append(newsContentBean.getImage_source()).append("</span>")
                .append("<img src=\"").append(newsContentBean.getImage())
                .append("\" alt=\"\">")
                .append("<div class=\"img-mask\"></div>");
        String mNewsContent = "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_content_style.css\"/>"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_header_style.css\"/>"
                + newsContentBean.getBody().replace("<div class=\"img-place-holder\">", stringBuilder.toString());
        return mNewsContent;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) MyApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
}
