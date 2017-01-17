package com.example.helldefender.rvfunction.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.activity.MainActivity;
import com.example.helldefender.rvfunction.entity.TEntity;
import com.example.helldefender.rvfunction.http.HttpMethods;
import com.example.helldefender.rvfunction.subscriber.ProgressSubscriber;
import com.example.helldefender.rvfunction.subscriber.SubscriberOnNextListener;
import com.example.helldefender.rvfunction.util.ActivityUtil;

/**
 * Created by Helldefender on 2016/10/25.
 */

public class SplashFragment extends BaseFragment {
    private TextView textView;
    private ImageView imageView;
    private Button button;
    private SubscriberOnNextListener subscriberOnNextListener;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        textView = (TextView) view.findViewById(R.id.splash_textview);
        imageView = (ImageView) view.findViewById(R.id.splash_imageview);
        button = (Button) view.findViewById(R.id.splash_button);
        subscriberOnNextListener = new SubscriberOnNextListener<TEntity>() {
            @Override
            public void onNext(TEntity tEntity) {
                textView.setText(tEntity.getText());
                ActivityUtil.handleImageByGlide(getHoldingActivity(),tEntity.getImg(),imageView);
            }
        };
        httpRequestGet();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                baseActivity.finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_splash;
    }

    public static SplashFragment getInstance() {
        return new SplashFragment();
    }

    protected void httpRequestGet() {
        HttpMethods.getInstance().getHttpInfo(new ProgressSubscriber<TEntity>(subscriberOnNextListener, getHoldingActivity()),101,1);
    }
}
