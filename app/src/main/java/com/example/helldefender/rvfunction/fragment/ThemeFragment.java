package com.example.helldefender.rvfunction.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.activity.AppActivity;
import com.example.helldefender.rvfunction.activity.NewContentActivity;
import com.example.helldefender.rvfunction.adapter.HeaderRvAdapter;
import com.example.helldefender.rvfunction.adapter.NewsRVAdapter;
import com.example.helldefender.rvfunction.entity.TEntity;
import com.example.helldefender.rvfunction.http.HttpMethods;
import com.example.helldefender.rvfunction.subscriber.ProgressSubscriber;
import com.example.helldefender.rvfunction.subscriber.SubscriberOnNextListener;
import com.example.helldefender.rvfunction.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helldefender on 2016/11/4.
 */

public class ThemeFragment extends BaseFragment {
    public static String THEME_FRAGMENT = "ThemeFragment";
    private int id;
    private LinearLayout mThemeContentLinearLayout;

    private SubscriberOnNextListener subscriberOnNextListener;

    private LinearLayout headLinearLayout;
    private LinearLayout.LayoutParams headLP;

    private RelativeLayout mRelativeLayout;
    private RelativeLayout.LayoutParams mRelativeLayoutParams;
    private ImageView mImageView;
    private RelativeLayout.LayoutParams imageRelativeLP;
    private TextView mThemeTextView;
    private RelativeLayout.LayoutParams themeTextLP;

    private LinearLayout mLinearLayout;
    private LinearLayout.LayoutParams linearLayoutParams;
    private TextView mAuthorTextView;
    private LinearLayout.LayoutParams authorTextLP;
    private RecyclerView mAuthorRecyclerView;
    private LinearLayout.LayoutParams authorRvLP;

    private RecyclerView mRecyclerView;
    private HeaderRvAdapter mHeaderRvAdapter;
    private NewsRVAdapter mNewsRvAdapter;
    private List<TEntity.StoriesBean> data;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            id = (int) getArguments().getSerializable(THEME_FRAGMENT);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        handleView(view);

        AppActivity appActivity = (AppActivity) baseActivity;
        appActivity.actionMenuView.getMenu().clear();
        appActivity.getMenuInflater().inflate(R.menu.theme_menu, appActivity.actionMenuView.getMenu());

        subscriberOnNextListener = new SubscriberOnNextListener<TEntity>() {
            @Override
            public void onNext(TEntity tEntity) {
                mThemeTextView.setText(tEntity.getDescription());
                ActivityUtil.handleImageByGlide(getHoldingActivity(), tEntity.getImage(), mImageView);
                for (TEntity.StoriesBean storiesBean : tEntity.getStories()) {
                    storiesBean.setData("1");
                    data.add(storiesBean);
                }
                mHeaderRvAdapter = new HeaderRvAdapter(mNewsRvAdapter);
                mHeaderRvAdapter.addHeaderView(headLinearLayout);
                mRecyclerView.setAdapter(mHeaderRvAdapter);
            }
        };
        httpRequestGet();
        mNewsRvAdapter.setOnItemClickLitener(new NewsRVAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("newsId", data.get(position - 1).getId());
                Intent intent = new Intent(getHoldingActivity(), NewContentActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_theme;
    }

    private void httpRequestGet() {
        HttpMethods.getInstance().getHttpInfo(new ProgressSubscriber<TEntity>(subscriberOnNextListener, getHoldingActivity()), id, 7);

    }

    private void handleView(View view) {
        mThemeContentLinearLayout = (LinearLayout) view.findViewById(R.id.layout_mainFragment);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getHoldingActivity()));
        data = new ArrayList<TEntity.StoriesBean>();

        headLinearLayout = new LinearLayout(getHoldingActivity());
        headLinearLayout.setOrientation(LinearLayout.VERTICAL);
        headLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headLinearLayout.setLayoutParams(headLP);

        mRelativeLayout = new RelativeLayout(getHoldingActivity());
        mRelativeLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        mRelativeLayout.setLayoutParams(mRelativeLayoutParams);

        mImageView = new ImageView(getHoldingActivity());
        mImageView.setImageResource(R.mipmap.ic_launcher);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageRelativeLP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        mImageView.setLayoutParams(imageRelativeLP);
        mRelativeLayout.addView(mImageView);

        mThemeTextView = new TextView(getHoldingActivity());
        mThemeTextView.setTextColor(Color.WHITE);
        mThemeTextView.setTextSize(25);
        themeTextLP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        themeTextLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        themeTextLP.leftMargin = 50;
        themeTextLP.rightMargin = 10;
        themeTextLP.bottomMargin = 20;
        mThemeTextView.setLayoutParams(themeTextLP);
        mRelativeLayout.addView(mThemeTextView);

        mLinearLayout = new LinearLayout(getHoldingActivity());
        mLinearLayout.setPadding(10, 10, 10, 10);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        mLinearLayout.setLayoutParams(linearLayoutParams);

        mAuthorTextView = new TextView(getHoldingActivity());
        mAuthorTextView.setTextSize(20);
        mAuthorTextView.setText("主编");
        authorTextLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        authorTextLP.topMargin = 20;
        mAuthorTextView.setLayoutParams(authorTextLP);
        mLinearLayout.addView(mAuthorTextView);

        mAuthorRecyclerView = new RecyclerView(getHoldingActivity());
        mAuthorRecyclerView.setLayoutManager(new LinearLayoutManager(getHoldingActivity()));
        authorRvLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        authorRvLP.leftMargin = 15;
        authorRvLP.topMargin = 5;
        authorRvLP.bottomMargin = 5;
        mAuthorRecyclerView.setLayoutParams(authorRvLP);
        mLinearLayout.addView(mAuthorRecyclerView);

        headLinearLayout.addView(mRelativeLayout);
        headLinearLayout.addView(mLinearLayout);

        mNewsRvAdapter = new NewsRVAdapter(data, getHoldingActivity());
    }

    public static ThemeFragment getInstance(int id) {
        ThemeFragment themeFragment = new ThemeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(THEME_FRAGMENT, id);
        themeFragment.setArguments(bundle);
        return themeFragment;
    }

}
