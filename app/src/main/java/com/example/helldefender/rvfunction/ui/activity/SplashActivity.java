package com.example.helldefender.rvfunction.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.data.entity.SplashBean;
import com.example.helldefender.rvfunction.server.HttpManager;
import com.example.helldefender.rvfunction.server.subscriber.ProgressSubscriber;
import com.example.helldefender.rvfunction.server.subscriber.SubscriberOnNextListener;
import com.example.helldefender.rvfunction.ui.activity.base.BaseActivity;
import com.example.helldefender.rvfunction.ui.fragment.base.BaseFragment;
import com.example.helldefender.rvfunction.util.ImageManager;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Helldefender on 2016/10/23.
 */

public class SplashActivity extends BaseActivity {
    private TextView textView;
    private ImageView splashImage;
    private SubscriberOnNextListener subscriberOnNextListener;
    private Subscription subscription;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void refreshViewMode(TypedValue background, int color, TypedValue toolbarBackground) {
    }

    @Override
    protected BaseFragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        handleStatusBar();

        textView = (TextView) findViewById(R.id.splash_textview);
        splashImage = (ImageView) findViewById(R.id.splash_imageview);

        subscriberOnNextListener = new SubscriberOnNextListener<SplashBean>() {
            @Override
            public void onNext(SplashBean splashBean) {
                textView.setText(splashBean.getText());
                ImageManager.handleImageByGlide(SplashActivity.this, splashBean.getImg(), splashImage);
                subscription = Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });


            }

            @Override
            public void onError(Throwable throwable) {
                if (throwable instanceof SocketTimeoutException || throwable instanceof ConnectException) {
                    subscription = Observable.timer(2000, TimeUnit.MILLISECONDS)
                            .subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                }
            }
        };

        httpRequestGet();
    }

    private void httpRequestGet() {
        HttpManager.getInstance().getNewSplash(new ProgressSubscriber<SplashBean>(subscriberOnNextListener, SplashActivity.this));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
