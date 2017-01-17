package com.example.helldefender.rvfunction.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.app.BaseActivity;
import com.example.helldefender.rvfunction.app.BaseFragment;
import com.example.helldefender.rvfunction.fragment.SplashFragment;

/**
 * Created by Helldefender on 2016/10/23.
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.splash_fragment_container;
    }

    @Override
    protected void refreshViewMode(TypedValue background, int color, TypedValue toolbarBackground) {
    }

    @Override
    protected BaseFragment getFragment() {
        return SplashFragment.getInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        handleStatusBar();

        if (getSupportFragmentManager().getFragments() == null) {
            BaseFragment baseFragment = getFragment();
            if (baseFragment != null) {
                addFragment(baseFragment);
            }
        }
    }
}
