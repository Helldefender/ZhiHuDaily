package com.example.helldefender.rvfunction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;

import com.example.helldefender.rvfunction.app.MyApplication;
import com.example.helldefender.rvfunction.app.BaseFragment;
import com.example.helldefender.rvfunction.fragment.CommentsFragment;

/**
 * Created by Helldefender on 2016/11/15.
 */

public class CommentsActivity extends SimpleActivity {
    private int newsId;
    private int comments;

    @Override
    protected BaseFragment getFragment() {
        return CommentsFragment.getInstance(newsId,comments);
    }

    @Override
    protected void refreshPartViewMode(TypedValue background, int color) {
        //对listview进行夜间模式处理,不用对toolbar进行处理,在simpleActivity中进行了处理
    }

    @Override
    protected void handlerIntent(Intent intent) {
        super.handlerIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            newsId = bundle.getInt("newsId");
            comments = bundle.getInt("comments");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //每次关闭评论时将是否已加载短评论置为false
        MyApplication.editor.putBoolean("isUpload", false).commit();
    }
}
