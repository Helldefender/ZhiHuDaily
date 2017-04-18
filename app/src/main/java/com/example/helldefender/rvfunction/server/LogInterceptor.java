package com.example.helldefender.rvfunction.server;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Helldefender on 2016/12/1.
 */

public class LogInterceptor implements Interceptor {
    private static String TAG = "LogIntercept";
    private static final Charset UTD0 = Charset.forName("utf-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        long time1 = System.nanoTime();
        response = chain.proceed(request);
        long time2 = System.nanoTime();
        double time = time2 - time1 / 1e6d;
        return null;
    }

}
