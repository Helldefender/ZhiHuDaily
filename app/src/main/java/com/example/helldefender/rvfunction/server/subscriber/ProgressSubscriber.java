package com.example.helldefender.rvfunction.server.subscriber;

import android.content.Context;

import com.example.helldefender.rvfunction.util.progress.ProgressCancelListener;
import com.example.helldefender.rvfunction.util.progress.ProgressDialogHandler;

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
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        onError(e);
        dismissProgressDialog();
    }

    @Override
    public void onNext(T t) {
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
