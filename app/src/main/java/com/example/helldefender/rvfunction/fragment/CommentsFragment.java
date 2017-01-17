package com.example.helldefender.rvfunction.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helldefender.rvfunction.app.BaseFragment;
import com.example.helldefender.rvfunction.app.MyApplication;
import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.adapter.CommentsRvAdapter;
import com.example.helldefender.rvfunction.adapter.HeaderRvAdapter;
import com.example.helldefender.rvfunction.entity.NewsCommentsBean;
import com.example.helldefender.rvfunction.http.HttpMethods;
import com.example.helldefender.rvfunction.subscriber.ProgressSubscriber;
import com.example.helldefender.rvfunction.subscriber.SubscriberOnNextListener;
import com.jude.utils.JUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helldefender on 2016/11/16.
 */

public class CommentsFragment extends BaseFragment {
    private int newsId;
    private int comments;
    private static String COMMENTS_FRAGMENT = "CommentsFragment";

    private SubscriberOnNextListener shortCommentsListener;
    private SubscriberOnNextListener longCommentsListener;

    private List<NewsCommentsBean.CommentsBean> longData;
    private List<NewsCommentsBean.CommentsBean> shortData;

    private RecyclerView mCommentsRV;
    private LinearLayoutManager mLinearLayoutManager;
    private HeaderRvAdapter headerRvAdapter;
    private CommentsRvAdapter commentsRvAdapter;

    private TextView longCommentsNum;
    //private RelativeLayout placeHolderLayout;
    private ImageView placeHolderImage;
    private TextView placeHolderText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newsId = (int) getArguments().getSerializable(COMMENTS_FRAGMENT);
            comments = (int) getArguments().getSerializable("comments");
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        handleView(view);

        baseActivity.getSupportActionBar().setTitle(comments + "条点评");

        longCommentsListener = new SubscriberOnNextListener<NewsCommentsBean>() {

            @Override

            public void onNext(NewsCommentsBean newsCommentsBean) {
                //不能够直接使用 longData=tEntity.getComments(); 否则无数据显示
                for (NewsCommentsBean.CommentsBean commentsBean : newsCommentsBean.getComments()) {
                    longData.add(commentsBean);
                }
                longCommentsNum.setText(longData.size() + "条长评论");

                headerRvAdapter = new HeaderRvAdapter(commentsRvAdapter);
                headerRvAdapter.addHeaderView(longCommentsNum);
                if (longData.size() == 0) {
                    headerRvAdapter.addHeaderView(placeHolderImage);
                    headerRvAdapter.addHeaderView(placeHolderText);
                }
                mCommentsRV.setAdapter(headerRvAdapter);
            }
        };

        shortCommentsListener = new SubscriberOnNextListener<NewsCommentsBean>() {

            @Override
            public void onNext(NewsCommentsBean newsCommentsBean) {
                if (MyApplication.sharedPreferences.getBoolean("isUpload", false)) {
                    shortData.clear();
                    MyApplication.editor.putBoolean("isUpload", false).commit();
                } else {
                    MyApplication.editor.putBoolean("isUpload", true).commit();
                    for (NewsCommentsBean.CommentsBean commentsBean : newsCommentsBean.getComments())
                        shortData.add(commentsBean);
                }

                headerRvAdapter.notifyDataSetChanged();
                if (longData.size() == 0) {

                }
                int position = longData.size() == 0 ? longData.size() + 3 : longData.size() + 1;
                mCommentsRV.scrollToPosition(position);
            }
        };

        httpRequest();

        commentsRvAdapter.setOnItemClickListener(new CommentsRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if (longData.size() == 0) {
                    if (position == longData.size() + 3) {
                        HttpMethods.getInstance().getNewsShortComments(new ProgressSubscriber<NewsCommentsBean>(shortCommentsListener, getHoldingActivity()), newsId);
                        return;
                    }
                }
                if (position == longData.size() + 1) {
                    HttpMethods.getInstance().getNewsShortComments(new ProgressSubscriber<NewsCommentsBean>(shortCommentsListener, getHoldingActivity()), newsId);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comments;
    }

    private void handleView(View view) {
        mCommentsRV = (RecyclerView) view.findViewById(R.id.comment_recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(getHoldingActivity());
        mCommentsRV.setLayoutManager(mLinearLayoutManager);

        longCommentsNum = new TextView(getHoldingActivity());
        longCommentsNum.setBackgroundResource(R.color.colorAccent);
        longCommentsNum.setPadding(JUtils.dip2px(15), JUtils.dip2px(5), JUtils.dip2px(5), JUtils.dip2px(5));
        longCommentsNum.setTextColor(getResources().getColor(android.R.color.black));
        longCommentsNum.setGravity(Gravity.CENTER_VERTICAL);
        AbsListView.LayoutParams longCommentsLv = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, JUtils.dip2px(50));
        longCommentsNum.setLayoutParams(longCommentsLv);

        placeHolderText = new TextView(getHoldingActivity());
        placeHolderText.setText("深度长评虚位以待");
        placeHolderText.setTextColor(getResources().getColor(android.R.color.black));
        placeHolderText.setGravity(Gravity.CENTER);
        AbsListView.LayoutParams placeHolderTextLP = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,JUtils.dip2px(50));
        placeHolderText.setLayoutParams(placeHolderTextLP);

        placeHolderImage = new ImageView(getHoldingActivity());
        placeHolderImage.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        placeHolderImage.setImageResource(R.mipmap.ic_launcher);
        AbsListView.LayoutParams placeHolderImageLP = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , JUtils.getScreenHeight()-JUtils.getActionBarHeight()-JUtils.dip2px(150));
        placeHolderImage.setLayoutParams(placeHolderImageLP);


        //placeHolderLayout = (RelativeLayout) view.findViewById(R.id.layout_placeholder);

        longData = new ArrayList<NewsCommentsBean.CommentsBean>();
        shortData = new ArrayList<NewsCommentsBean.CommentsBean>();
        commentsRvAdapter = new CommentsRvAdapter(getHoldingActivity(), shortData, longData, comments);

    }

    public static CommentsFragment getInstance(int newsId, int comments) {
        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(COMMENTS_FRAGMENT, newsId);
        bundle.putSerializable("comments", comments);
        commentsFragment.setArguments(bundle);
        return commentsFragment;
    }

    private void httpRequest() {
        HttpMethods.getInstance().getNewsLongComments(new ProgressSubscriber<NewsCommentsBean>(longCommentsListener, getHoldingActivity()), newsId);
    }
}
