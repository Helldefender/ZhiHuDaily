package com.example.helldefender.rvfunction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.app.BaseActivity;
import com.example.helldefender.rvfunction.app.BaseFragment;
import com.example.helldefender.rvfunction.util.DayNightHelper;

/**
 * Created by Helldefender on 2016/11/16.
 */

public abstract class SimpleActivity extends BaseActivity {
    public Toolbar toolbar;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_base_simple;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment_container_simple;
    }


    @Override
    protected void refreshViewMode(TypedValue background, int color, TypedValue toolbarBackground) {
        toolbar.setBackgroundResource(toolbarBackground.resourceId);
        refreshPartViewMode(background, color);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDayNightHelper = DayNightHelper.getInstance(this);
        initTheme();
        setContentView(getContentViewId());

        initView();

        handleStatusBar();

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        if (null != getIntent()) {
            handlerIntent(getIntent());
        }

        //避免重复添加fragment
        if (getSupportFragmentManager().getFragments() == null) {
            BaseFragment baseFragment = getFragment();
            if (baseFragment != null) {
                addFragment(baseFragment);
            }
        }
    }

    protected abstract BaseFragment getFragment();

    protected void handlerIntent(Intent intent) {
    }

    protected abstract void refreshPartViewMode(TypedValue background, int color);

    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.activity_base_simple_toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击 toolbar 返回键 finish 当前activity 系统原生返回键
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
