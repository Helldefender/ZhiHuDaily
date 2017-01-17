package com.example.helldefender.rvfunction.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.app.BaseFragment;
import com.example.helldefender.rvfunction.view.DownloadProgressBar;

/**
 * Created by Helldefender on 2016/11/27.
 */

public class ProgressBarFragment extends BaseFragment implements View.OnClickListener, Runnable {
    DownloadProgressBar mDownloadProgressBar;
//    private DownloadProgressBar mDownloadProgressBar;
//    private Thread mDownloadThread;
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            mDownloadProgressBar.setProgress(msg.arg1);
//            if (msg.arg1 == 100) {
//                mDownloadProgressBar.finishLoad();
//            }
//        }
//    };
// }
//
    public static ProgressBarFragment getInstance() {
        return new ProgressBarFragment();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mDownloadProgressBar = (DownloadProgressBar) view.findViewById(R.id.downloadProgressBar);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_progressbar;
    }

    @Override
    public void run() {

    }

}
