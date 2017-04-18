package com.example.helldefender.rvfunction.server.subscriber;

/**
 * Created by Helldefender on 2016/10/27.
 */

public interface SubscriberOnNextListener<T>{
    void onNext(T t);

    void onError(Throwable throwable);
}
