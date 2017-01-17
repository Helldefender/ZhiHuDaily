package com.example.helldefender.rvfunction.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.activity.MainActivity;
import com.example.helldefender.rvfunction.app.BaseFragment;
import com.example.helldefender.rvfunction.entity.SplashBean;
import com.example.helldefender.rvfunction.http.HttpMethods;
import com.example.helldefender.rvfunction.subscriber.ProgressSubscriber;
import com.example.helldefender.rvfunction.subscriber.SubscriberOnNextListener;
import com.example.helldefender.rvfunction.util.ActivityUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Helldefender on 2016/10/25.
 */

public class SplashFragment extends BaseFragment {
    private TextView textView;
    private ImageView splashImage;
//    private ImageView taijiImage;
//    private ImageView hourglassImage;
    private SubscriberOnNextListener subscriberOnNextListener;
    private Subscription subscription;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        handleView(view);

        subscriberOnNextListener = new SubscriberOnNextListener<SplashBean>() {
            @Override
            public void onNext(SplashBean splashBean) {
                textView.setText(splashBean.getText());
                ActivityUtil.handleImageByGlide(getHoldingActivity(),splashBean.getImg(),splashImage);
               subscription=Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                Intent intent = new Intent(getHoldingActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        });
            }
        };

        httpRequestGet();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_splash;
    }

    public static SplashFragment getInstance() {
        return new SplashFragment();
    }

    protected void httpRequestGet() {
        HttpMethods.getInstance().getNewSplash(new ProgressSubscriber<SplashBean>(subscriberOnNextListener, getHoldingActivity()));
    }

    private void handleView(View view) {
        textView = (TextView) view.findViewById(R.id.splash_textview);
        splashImage = (ImageView) view.findViewById(R.id.splash_imageview);
//        taijiImage = (ImageView) view.findViewById(R.id.imageview_taiji);
//        hourglassImage = (ImageView) view.findViewById(R.id.imageview_hourglass);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
