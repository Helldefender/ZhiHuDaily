package com.example.helldefender.rvfunction.subscriber;

import android.content.Context;

import com.example.helldefender.rvfunction.util.ProgressCancelListener;
import com.example.helldefender.rvfunction.util.ProgressDialogHandler;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by Helldefender on 2016/10/27.
 */

public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {
    private SubscriberOnNextListener subscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Context context;


    public ProgressSubscriber(SubscriberOnNextListener subscriberOnNextListener, Context context) {
        this.subscriberOnNextListener = subscriberOnNextListener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(this, context, true);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onStart() {
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        //停止,取消progressdialog progressdialog
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        //集中处理错误
        if (e instanceof SocketTimeoutException) {
        } else if (e instanceof ConnectException) {
        } else {
        }
        dismissProgressDialog();
    }

    @Override
    public void onNext(T t) {
        // 启动 显示 停止
        //抽象类，抽象方法 抽象
        if (subscriberOnNextListener != null) {
            subscriberOnNextListener.onNext(t);
        }
    }

    @Override
    public void onCancelProgress() {
        if (this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
