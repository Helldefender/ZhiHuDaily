package com.example.helldefender.rvfunction.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Helldefender on 2016/10/19.
 */

public abstract class BaseFragment extends Fragment{
    protected BaseActivity baseActivity;

    protected abstract void initView(View view, Bundle savedInstanceState);

    //获取布局文件Id
    protected abstract int getLayoutId();

    //获取宿主activity
    protected BaseActivity getHoldingActivity() {
        return baseActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.baseActivity = (BaseActivity) context;
    }

    protected void addFragment(BaseFragment baseFragment) {
        if (baseFragment != null) {
            getHoldingActivity().addFragment(baseFragment);
        }
    }

    protected void removeFragment() {
        getHoldingActivity().removeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

}
