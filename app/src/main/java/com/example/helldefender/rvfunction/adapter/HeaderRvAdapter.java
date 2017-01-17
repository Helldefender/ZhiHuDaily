package com.example.helldefender.rvfunction.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Helldefender on 2016/10/19.
 */

public class HeaderRvAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int BASE_HEADERVIEW_ITEM = 10000;
    // headerview footerview footer 都是强引用
    //
    private SparseArrayCompat<View> headerViews = new SparseArrayCompat<View>();
    private RecyclerView.Adapter mInnerAdapter;

    public HeaderRvAdapter(RecyclerView.Adapter mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;
    }

    private boolean isHeaderViewPosition(int position) {
        return position < getHeadersCount();
    }

    private int getHeadersCount() {
        return headerViews.size();
    }

    public void addHeaderView(View view) {
        headerViews.put(headerViews.size() + BASE_HEADERVIEW_ITEM, view);
    }

    private int getRealItemCount(){
        return mInnerAdapter.getItemCount();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(headerViews.get(viewType)!=null){
            ViewHolder viewholder=ViewHolder.createViewHolder(parent.getContext(),headerViews.get(viewType));
            return viewholder;
        }
        return mInnerAdapter.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(isHeaderViewPosition(position))
            return;
        mInnerAdapter.onBindViewHolder(holder,position-getHeadersCount());

    }

    @Override
    public int getItemCount() {
        return getHeadersCount()+getRealItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPosition(position)){
            return headerViews.keyAt(position);
        }
        return mInnerAdapter.getItemViewType(position-getHeadersCount());
    }

    //gridlayoutmanager
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager=recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager= (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup=gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if(headerViews.get(viewType)!= null ){
                        return ((GridLayoutManager) layoutManager).getSpanCount();
                    }
                    if(spanSizeLookup != null){
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;

                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    public void onViewAttachToWindow(RecyclerView.ViewHolder viewHolder) {
        mInnerAdapter.onViewAttachedToWindow(viewHolder);
        int position=viewHolder.getLayoutPosition();
        if (isHeaderViewPosition(position)) {
            ViewGroup.LayoutParams layoutparams=viewHolder.itemView.getLayoutParams();
            if (layoutparams != null && layoutparams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams staggeredGridLayoutManagerParams = (StaggeredGridLayoutManager.LayoutParams) layoutparams;
                staggeredGridLayoutManagerParams.setFullSpan(true);
            }
        }
    }
}

