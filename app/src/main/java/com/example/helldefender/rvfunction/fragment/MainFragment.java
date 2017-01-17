package com.example.helldefender.rvfunction.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.example.helldefender.rvfunction.adapter.ImageAdapter;
import com.example.helldefender.rvfunction.adapter.NewsRVAdapter;
import com.example.helldefender.rvfunction.entity.TEntity;
import com.example.helldefender.rvfunction.http.HttpMethods;
import com.example.helldefender.rvfunction.subscriber.ProgressSubscriber;
import com.example.helldefender.rvfunction.subscriber.SubscriberOnNextListener;
import com.example.helldefender.rvfunction.util.ActivityUtil;
import com.jude.utils.JUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helldefender on 2016/10/29.
 */

public class MainFragment extends BaseFragment {

    private RelativeLayout mRelativeLayout;
    private RelativeLayout.LayoutParams relativeLP;
    private LinearLayout mLinearLayout;
    private ViewPager mViewPager;
    private RelativeLayout.LayoutParams viewPagerLP;
    private RelativeLayout.LayoutParams linearLP;

    private ImageView[] imageIndicators;

    private ArrayList<RelativeLayout> views;
    private ImageHandler handler = new ImageHandler(new WeakReference<MainFragment>((this)));

    public RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private HeaderRvAdapter mHeaderRvAdapter;
    private NewsRVAdapter newsRVAdapter;

    private SubscriberOnNextListener subscriberOnNextListener;
    private SubscriberOnNextListener loadingMoreListener;
    private List<TEntity.StoriesBean> storiesList;
    private List<TEntity.TopStoriesBean> topStoriesList;

    private ActionMenuView actionMenuView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton floatingActionButton;

    @Override

    protected void initView(View view, Bundle savedInstanceState) {
        handleView(view);

//        AppActivity appActivity = (AppActivity) baseActivity;
//        actionMenuView = appActivity.actionMenuView;
//        actionMenuView.getMenu().clear();
//        appActivity.getMenuInflater().inflate(R.menu.setting, actionMenuView.getMenu());
//        actionMenuView.setOnMenuItemClickListener(this);

        ((AppActivity) baseActivity).toolbar.setTitle("知乎日报");

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHeaderRvAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },1500);
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright), getResources().getColor(android.R.color.holo_green_light), getResources().getColor(android.R.color.holo_orange_light));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.scrollToPosition(0);
                baseActivity.getSupportActionBar().setTitle("知乎日报");
            }
        });

        subscriberOnNextListener = new SubscriberOnNextListener<TEntity>() {
            @Override
            public void onNext(TEntity tEntity) {
                int i = 0;
                for (TEntity.StoriesBean storiesBean : tEntity.getStories()) {
                    storiesBean.setData(tEntity.getDate());
                    storiesList.add(storiesBean);
                }
                for (TEntity.TopStoriesBean topStoriesBean : tEntity.getTop_stories()) {
                    ActivityUtil.handleImageByGlide(getHoldingActivity(), topStoriesBean.getImage(), (ImageView) views.get(i).findViewById(R.id.item_viewpager_image));
                    handleText((TextView) views.get(i).findViewById(R.id.item_viewpager_textview), topStoriesBean.getTitle());
                    i++;
                }

                mHeaderRvAdapter = new HeaderRvAdapter(newsRVAdapter);
                mHeaderRvAdapter.addHeaderView(mRelativeLayout);
                mRecyclerView.setAdapter(mHeaderRvAdapter);
            }
        };

        httpRequestGet();

        loadingMoreListener = new SubscriberOnNextListener<TEntity>() {
            @Override
            public void onNext(TEntity tEntity) {
                for (TEntity.StoriesBean storiesBean : tEntity.getStories()) {
                    storiesBean.setData(tEntity.getDate());
                    storiesList.add(storiesBean);
                }
                newsRVAdapter.notifyDataSetChanged();
                mHeaderRvAdapter.notifyDataSetChanged();
            }
        };

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //配合adapter的currentItem字段进行设置
            @Override
            public void onPageSelected(int position) {
                handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, position, 0));
                int total = imageIndicators.length;
                position %= total;
                for (int i = 0; i < total; i++) {
                    if (i == position) {
                        imageIndicators[i].setImageResource(R.color.colorAccent);
                    } else {
                        imageIndicators[i].setImageResource(R.color.colorPrimary);
                    }
                }
            }

            //复写该方法实现轮播效果的暂停和恢复
            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });
        //默认在中间，使用户看不到边界
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        //开始轮播
        handler.sendEmptyMessageDelayed(ImageHandler.MSG_KEEP_SILENT, ImageHandler.MSG_DELAY);

        newsRVAdapter.setOnItemClickLitener(new NewsRVAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("newsId", storiesList.get(position - 1).getId());
                Intent intent = new Intent(getHoldingActivity(), NewContentActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                        if (firstVisibleItem > 0)
                            baseActivity.getSupportActionBar().setTitle(storiesList.get(firstVisibleItem - 1).getWeekend());
                        int lastVisibablePosition = mLinearLayoutManager.findLastVisibleItemPosition();
                        if (lastVisibablePosition >= mLinearLayoutManager.getItemCount() - 2) {
                            final int preDate = Integer.parseInt(storiesList.get(lastVisibablePosition - 1).getData());
                            httpRequestLoadMore(preDate);
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void handleView(View view) {
        mRelativeLayout = new RelativeLayout(getHoldingActivity());
        relativeLP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, JUtils.dip2px(350));
        mRelativeLayout.setLayoutParams(relativeLP);

        mViewPager = new ViewPager(getHoldingActivity());
        viewPagerLP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, JUtils.dip2px(350));
        mViewPager.setLayoutParams(viewPagerLP);
        mRelativeLayout.addView(mViewPager);

        views = new ArrayList<RelativeLayout>();
        handleViewPageItem();

        mLinearLayout = new LinearLayout(getHoldingActivity());
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER);
        linearLP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, JUtils.dip2px(30));
        linearLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        linearLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        linearLP.setMargins(50, 50, 50, 15);
        mLinearLayout.setLayoutParams(linearLP);
        mRelativeLayout.addView(mLinearLayout);

        imageIndicators = new ImageView[5];
        handleIndicator();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(getHoldingActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        storiesList = new ArrayList<TEntity.StoriesBean>();
        topStoriesList = new ArrayList<TEntity.TopStoriesBean>();
        newsRVAdapter = new NewsRVAdapter(storiesList, getHoldingActivity());

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
    }

    private void handleIndicator() {
        ImageView imageView;
        for (int i = 0; i < 5; i++) {
            imageView = new ImageView(getHoldingActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(11, 11);
            layoutParams.setMargins(7, 10, 7, 10);
            imageView.setLayoutParams(layoutParams);
            imageIndicators[i] = imageView;
            if (i == 0) {
                imageIndicators[i].setBackgroundResource(R.color.colorAccent);
            } else {
                imageIndicators[i].setBackgroundResource(R.color.colorPrimary);
            }
            ((ViewGroup) mLinearLayout).addView(imageView);
        }
    }

    private void handleViewPageItem() {
        views.add((RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_viewpager, null));
        views.add((RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_viewpager, null));
        views.add((RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_viewpager, null));
        views.add((RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_viewpager, null));
        views.add((RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_viewpager, null));
        mViewPager.setAdapter(new ImageAdapter(views));
    }

    private void handleText(TextView textView, String string) {
        textView.setText(string);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    private void httpRequestGet() {
        HttpMethods.getInstance().getHttpInfo(new ProgressSubscriber<TEntity>(subscriberOnNextListener, getHoldingActivity()), 102, 1);

    }

    private void httpRequestLoadMore(int preData) {
        HttpMethods.getInstance().getHttpInfo(new ProgressSubscriber<TEntity>(loadingMoreListener, getHoldingActivity()), preData, 3);
    }

    public static MainFragment getInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        return false;
//    }

    private static class ImageHandler extends Handler {
        //请求更新显示view
        protected static final int MSG_UPDATE_IMAGE = 1;
        //请求暂停轮播
        protected static final int MSG_KEEP_SILENT = 2;
        //请求恢复轮播
        protected static final int MSG_BREAK_SILENT = 3;
        //记录最新的页号，当用户用手滑动时，需要更新页号
        protected static final int MSG_PAGE_CHANGED = 4;
        //轮播间隔时间
        protected static final long MSG_DELAY = 3000;
        //使用弱引用避免handler内存泄漏 泛型参数可以为activity或者为fragment
        private WeakReference<MainFragment> weakReference;
        private int currentItem = 0;

        public ImageHandler(WeakReference<MainFragment> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainFragment fragment = weakReference.get();
            //activiyy已经回收，无需再处理ui
            if (fragment == null) {
                return;
            }
            //检查消息队列并移除未发送的消息，避免在复杂环境下消息出现问题
            if (fragment.handler.hasMessages(MSG_UPDATE_IMAGE)) {
                fragment.handler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                //请求更新显示的view
                case MSG_UPDATE_IMAGE:
                    currentItem++;
                    fragment.mViewPager.setCurrentItem(currentItem);
                    //准备下次轮播
                    fragment.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                //请求暂停轮播
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                //请求恢复轮播
                case MSG_BREAK_SILENT:
                    //记录当前页号，避免播放的是时候页面显示不正确
                    fragment.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    currentItem = msg.arg1;
                    break;
                default:
                    break;
            }
        }
    }
}
