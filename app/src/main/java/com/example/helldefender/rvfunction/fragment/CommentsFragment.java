package com.example.helldefender.rvfunction.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.adapter.ListAdapter;
import com.example.helldefender.rvfunction.entity.TEntity;
import com.example.helldefender.rvfunction.http.HttpMethods;
import com.example.helldefender.rvfunction.subscriber.ProgressSubscriber;
import com.example.helldefender.rvfunction.subscriber.SubscriberOnNextListener;
import com.example.helldefender.rvfunction.view.MyListView;
import com.example.helldefender.rvfunction.view.MyScrollView;
import com.example.helldefender.rvfunction.view.ScrollViewListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.helldefender.rvfunction.R.id.scroller;

/**
 * Created by Helldefender on 2016/11/16.
 */

public class CommentsFragment extends BaseFragment{
    private int newsId;
    private int comments;
    private static String COMMENTS_FRAGMENT = "CommentsFragment";

    private RelativeLayout mRelativeLayout;
    private MyListView longList;
    private MyListView shortList;
    private MyScrollView mScrollView;
    private ListAdapter longListAdapter;
    private ListAdapter shortListAdapter;

    private SubscriberOnNextListener shortSubscriberListener;
    private SubscriberOnNextListener longSubscriberListener;

    private List<TEntity.CommentsBean> longData;
    private List<TEntity.CommentsBean> shortData;

    private TextView longCommentsNum;

    private LinearLayout linearLayout;
    private TextView shortCommentsNum;
    private Button moreBtn;

    private ListView listView;

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

        baseActivity.getSupportActionBar().setTitle(comments+"条点评");

        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mScrollView.setAllowChildViewScroll(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        mScrollView.setAllowChildViewScroll(true);
                        break;
                }
                return false;
            }
        });

        longSubscriberListener = new SubscriberOnNextListener<TEntity>() {

            @Override
            public void onNext(TEntity tEntity) {
                longData = tEntity.getComments();
                longListAdapter = new ListAdapter(getHoldingActivity(), R.layout.item_comments, longData);
                longList.addHeaderView(longCommentsNum);
                longList.addFooterView(linearLayout);
                longList.setAdapter(longListAdapter);
                longList.setScrollView(mScrollView);
            }
        };
        shortSubscriberListener = new SubscriberOnNextListener<TEntity>() {

            @Override
            public void onNext(TEntity tEntity) {
                shortData = tEntity.getComments();
                shortListAdapter = new ListAdapter(getHoldingActivity(), R.layout.item_comments, shortData);
                shortList.setAdapter(shortListAdapter);
                shortList.setScrollView(mScrollView);
            }
        };

        httpRequest();

        longList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //判断滑动到底部,scrollvew滚动，屏蔽掉listview的滑动功能
                        if (view.getLastVisiblePosition() == view.getCount() - 1) {
                            longList.setNotAllowParentScroll(false);
                            mScrollView.setAllowChildViewScroll(false);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        shortList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == view.getCount() - 1) {
                            mScrollView.setAllowChildViewScroll(false);
                            shortList.setNotAllowParentScroll(false);
                            mScrollView.requestDisallowInterceptTouchEvent(true);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mScrollView.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                int[] shortListPosition = new int[2];
                shortList.getLocationOnScreen(shortListPosition);
                int[] shortLocation = new int[2];
                mScrollView.getLocationOnScreen(shortLocation);
                if (shortListPosition[1] == shortLocation[1]) {
                    shortList.setNotAllowParentScroll(true);
                    mScrollView.setAllowChildViewScroll(true);
                    return;
                }
                int[] longListPosition = new int[2];
                longList.getLocationOnScreen(longListPosition);
                int[] longLocation = new int[2];
                mScrollView.getLocationOnScreen(longLocation);
                if (longListPosition[1] == longLocation[1]) {
                    shortList.setNotAllowParentScroll(true);
                    mScrollView.setAllowChildViewScroll(true);
                    return;
                }
                mScrollView.setAllowChildViewScroll(false);
            }
        });

        mRelativeLayout.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams LongLayoutParams = (LinearLayout.LayoutParams) longList.getLayoutParams();
                LongLayoutParams.height=mRelativeLayout.getHeight();
                longList.setLayoutParams(LongLayoutParams);
                LinearLayout.LayoutParams shortLayoutParams = (LinearLayout.LayoutParams) shortList.getLayoutParams();
                shortLayoutParams.height=mRelativeLayout.getHeight();
                longList.setLayoutParams(shortLayoutParams);

            }
        });

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<10;i++) {
                    TEntity.CommentsBean commentsBean=new TEntity.CommentsBean();
                    commentsBean.setAuthor("test");
                    commentsBean.setContent("test");
                    commentsBean.setLikes(5);
                    commentsBean.setAvatar("test");
                    longData.add(commentsBean);
                }
                longListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comments;
    }

    private void handleView(View view) {
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.layout_comments);
        mScrollView = (MyScrollView) view.findViewById(scroller);
        longList = (MyListView) view.findViewById(R.id.long_listView);
        shortList = (MyListView) view.findViewById(R.id.short_listView);

        longCommentsNum = new TextView(getHoldingActivity());
        longCommentsNum.setText("10");
        AbsListView.LayoutParams longCommentsLv = new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        longCommentsNum.setLayoutParams(longCommentsLv);

        linearLayout = new LinearLayout(getHoldingActivity());
        AbsListView.LayoutParams linearLayoutLv = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(linearLayoutLv);

        shortCommentsNum = new TextView(getHoldingActivity());
        shortCommentsNum.setText("20");
        LinearLayout.LayoutParams shortCommentsLv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        shortCommentsNum.setLayoutParams(shortCommentsLv);
        linearLayout.addView(shortCommentsNum);

        moreBtn = new Button(getHoldingActivity());
        moreBtn.setText("1");
        LinearLayout.LayoutParams moreBtnLv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.addView(moreBtn);


        longData = new ArrayList<TEntity.CommentsBean>();
        shortData = new ArrayList<TEntity.CommentsBean>();

    }

    public static CommentsFragment getInstance(int newsId,int comments) {
        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(COMMENTS_FRAGMENT, newsId);
        bundle.putSerializable("comments", comments);
        commentsFragment.setArguments(bundle);
        return commentsFragment;
    }

    private void httpRequest() {
        //long
        HttpMethods.getInstance().getHttpInfo(new ProgressSubscriber<TEntity>(longSubscriberListener, getHoldingActivity()), newsId, 5);
        //short
        HttpMethods.getInstance().getHttpInfo(new ProgressSubscriber<TEntity>(shortSubscriberListener, getHoldingActivity()), newsId, 6);

    }
}
