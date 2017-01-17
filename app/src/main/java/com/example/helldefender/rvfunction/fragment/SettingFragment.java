package com.example.helldefender.rvfunction.fragment;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.activity.MainActivity;

/**
 * Created by Helldefender on 2016/11/9.
 */

public class SettingFragment extends BaseFragment {
    private SwitchCompat mSwitchCompat;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        baseActivity.getSupportActionBar().setTitle("设置");
        handleView(view);
        mSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity settingActivity = (MainActivity) getHoldingActivity();
                settingActivity.getSupportActionBar().setTitle("设置");
                //modeChange-dayNightMode
                settingActivity.modeChange();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    public static SettingFragment getInstance() {
        return new SettingFragment();
    }

    private void handleView(View view) {
        mSwitchCompat = (SwitchCompat) view.findViewById(R.id.switchCompat);
    }
}
