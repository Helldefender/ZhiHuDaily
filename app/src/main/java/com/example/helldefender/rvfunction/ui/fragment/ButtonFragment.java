package com.example.helldefender.rvfunction.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.ui.fragment.base.BaseFragment;

/**
 * Created by Helldefender on 2016/11/24.
 */

public class ButtonFragment extends BaseFragment {
    private Button mDownloadButton;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mDownloadButton = (Button) view.findViewById(R.id.download_button);
        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDownloadPart(ProgressBarFragment.getInstance());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_button;
    }

    public static ButtonFragment getInstance() {
        return new ButtonFragment();
    }

    private void initDownloadPart(BaseFragment baseFragment) {
        getHoldingActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.download_frameLayout, baseFragment, baseFragment.getClass().getSimpleName())
                .addToBackStack(baseFragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }
}
