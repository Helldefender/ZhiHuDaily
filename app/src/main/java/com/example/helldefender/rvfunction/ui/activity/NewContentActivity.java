package com.example.helldefender.rvfunction.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helldefender.rvfunction.server.HttpManager;
import com.example.helldefender.rvfunction.ui.activity.base.BaseActivity;
import com.example.helldefender.rvfunction.app.MyApplication;
import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.data.entity.NewsBean;
import com.example.helldefender.rvfunction.data.entity.NewsContentBean;
import com.example.helldefender.rvfunction.data.entity.NewsExtraBean;
import com.example.helldefender.rvfunction.ui.fragment.base.BaseFragment;
import com.example.helldefender.rvfunction.server.subscriber.ProgressSubscriber;
import com.example.helldefender.rvfunction.server.subscriber.SubscriberOnNextListener;
import com.example.helldefender.rvfunction.util.DisplayUtil;
import com.example.helldefender.rvfunction.util.DayNightHelper;

/**
 * Created by Helldefender on 2016/11/17.
 */

public class NewContentActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private static String NEWS_CONTENT_FRAGMENT = "NewsContentFragment";
    private int newsId;

    private WebView mWebView;

    private SubscriberOnNextListener newsContentListener;
    private SubscriberOnNextListener newsExtraListener;

    private ImageView share;
    private ImageView collection;
    private TextView popularityTV;
    private ImageView popularityIcon;
    private TextView commentsTV;
    private ImageView commentsIcon;

    private int comments;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_newscontent;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void refreshViewMode(TypedValue background, int color, TypedValue toolbarBackground) {
        //对除了toolbar外的其他控件进行夜间模式的处理
    }

    @Override
    protected BaseFragment getFragment() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击 toolbar 返回键 finish 当前activity 系统原生返回键
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDayNightHelper = DayNightHelper.getInstance(this);
        initTheme();
        setContentView(getContentViewId());

        newsId = getIntent().getIntExtra("newsId", 0);

        initView();

        handleStatusBar();

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        newsContentListener = new SubscriberOnNextListener<NewsContentBean>() {
            @Override
            public void onNext(NewsContentBean newsContentBean) {
                mWebView.loadDataWithBaseURL("file:///android_asset/", DisplayUtil.structHtml(newsContentBean), "text/html", "UTF-8", null);
            }
        };

        newsExtraListener = new SubscriberOnNextListener<NewsExtraBean>() {
            @Override
            public void onNext(NewsExtraBean newsExtraBean) {
                popularityTV.setText(newsExtraBean.getPopularity() + "");
                commentsTV.setText(newsExtraBean.getComments() + "");
                comments = newsExtraBean.getComments();
            }
        };

        httpRequestGet();

        share.setOnClickListener(this);
        collection.setOnClickListener(this);
        commentsTV.setOnClickListener(this);
        commentsIcon.setOnClickListener(this);
        popularityIcon.setOnClickListener(this);
        popularityTV.setOnClickListener(this);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.newsContent_toolbar);
        mWebView = (WebView) findViewById(R.id.web_View);
        popularityTV = (TextView) findViewById(R.id.popularity);
        commentsTV = (TextView) findViewById(R.id.comments);
        collection = (ImageView) findViewById(R.id.collect);
        share = (ImageView) findViewById(R.id.share);
        popularityIcon = (ImageView) findViewById(R.id.popularity_icon);
        commentsIcon = (ImageView) findViewById(R.id.comments_icon);

    }

    private void httpRequestGet() {
        //获取不到数据的原因:单例模式
        HttpManager.getInstance().getNewsContent(new ProgressSubscriber<NewsBean>(newsContentListener, this), newsId);
        HttpManager.getInstance().getNewsExtra(new ProgressSubscriber<NewsExtraBean>(newsExtraListener,this) ,newsId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                break;
            case R.id.collect:
                if (MyApplication.infoStorageManager.getId().contains(new Integer(newsId))) {
                    MyApplication.infoStorageManager.deleteId(newsId);
                } else {
                    MyApplication.infoStorageManager.saveId(newsId);
                }
                break;
            case R.id.popularity_icon:
            case R.id.popularity:
                //改变背景，进行颜色的存储
                break;
            case R.id.comments_icon:
            case R.id.comments:
                Bundle bundle = new Bundle();
                bundle.putInt("newsId", newsId);
                bundle.putInt("comments", comments);
                Intent intent = new Intent(this, CommentsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }

    }
}
