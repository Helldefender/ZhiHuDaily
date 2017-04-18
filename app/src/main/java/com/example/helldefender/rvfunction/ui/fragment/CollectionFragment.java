package com.example.helldefender.rvfunction.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.server.HttpManager;
import com.example.helldefender.rvfunction.ui.activity.NewContentActivity;
import com.example.helldefender.rvfunction.ui.adapter.NewsRVAdapter;
import com.example.helldefender.rvfunction.ui.fragment.base.BaseFragment;
import com.example.helldefender.rvfunction.app.MyApplication;
import com.example.helldefender.rvfunction.data.entity.NewsBean;
import com.example.helldefender.rvfunction.data.entity.NewsContentBean;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Helldefender on 2016/11/24.
 */

public class CollectionFragment extends BaseFragment {
    private RecyclerView mCollectionRV;
    private NewsRVAdapter newsRVAdapter;
    private List<NewsBean.StoriesBean> data;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        handleView(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        data = new ArrayList<NewsBean.StoriesBean>();
        newsRVAdapter = new NewsRVAdapter(data, getHoldingActivity());
        mLinearLayoutManager = new LinearLayoutManager(getHoldingActivity());
        mCollectionRV.setLayoutManager(mLinearLayoutManager);

        Observable.from(MyApplication.infoStorageManager.getId())
                .flatMap(new Func1<Integer, Observable<NewsContentBean>>() {
                    @Override
                    public Observable<NewsContentBean> call(Integer integer) {
                        return HttpManager.getInstance().getNewsContentObservable(integer.intValue());
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<NewsContentBean>>() {
                    @Override
                    public void call(List<NewsContentBean> newsContentBeen) {
                        for (NewsContentBean newsContentBean : newsContentBeen) {
                            NewsBean.StoriesBean storiesBean = new NewsBean.StoriesBean();
                            storiesBean.setTitle(newsContentBean.getTitle());
                            storiesBean.setId(newsContentBean.getId());
                            storiesBean.setData("N0_DATE");
                            storiesBean.setImages(newsContentBean.getImages());
                            data.add(storiesBean);
                        }
                        mCollectionRV.setAdapter(newsRVAdapter);
                    }
                });
//        newsContentListener = new SubscriberOnNextListener<NewsContentBean>() {
//            @Override
//            public void onNext(NewsContentBean newsContentBean) {
//                i++;
//                NewsBean.StoriesBean storiesBean = new NewsBean.StoriesBean();
//                storiesBean.setTitle(newsContentBean.getTitle());
//                storiesBean.setId(newsContentBean.getId());
//                storiesBean.setData("NO_DATE");
//                storiesBean.setImages(newsContentBean.getImages());
//                data.add(storiesBean);
//                if (i == MyApplication.infoStorageManager.getId().size()) {
//                    mCollectionRV.setAdapter(newsRVAdapter);
//                }
//            }
//        };
        //这里可以改写尝试使用zip
//        for (int i = 0; i < MyApplication.infoStorageManager.getId().size(); i++)
//            HttpManager.getInstance().getNewsContent(new ProgressSubscriber<NewsContentBean>(newsContentListener, getHoldingActivity()), MyApplication.infoStorageManager.getId().get(i).intValue());

        newsRVAdapter.setOnItemClickListener(new NewsRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("newsId", data.get(position).getId());
                Intent intent = new Intent(getHoldingActivity(), NewContentActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collection;
    }

    private void handleView(View view) {
        mCollectionRV = (RecyclerView) view.findViewById(R.id.collection_recyclerview);
    }

    public static CollectionFragment getInstance() {
        return new CollectionFragment();
    }

}
