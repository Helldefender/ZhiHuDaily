package com.example.helldefender.rvfunction.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.entity.NewsBean;
import com.example.helldefender.rvfunction.util.ActivityUtil;

import java.util.List;

/**
 * Created by Helldefender on 2016/11/4.
 */

public class NewsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewsBean.StoriesBean> data;
    private Context mContext;
    public static final int NORMAL_ITEM = 0;
    public static final int HEADER_ITEM = 1;

    public NewsRVAdapter(List<NewsBean.StoriesBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL_ITEM) {
            return new NormalItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_normal_new, parent, false));
        }
        return new HeadItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head_news, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        NewsBean.StoriesBean story = data.get(position);
        //必须先HeadItemHolder,继承关系，继承自NormalItemHolder
        if (holder instanceof HeadItemHolder) {
            bindHeadItem(story, (HeadItemHolder) holder);
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                    }
                });
            }
        } else if (holder instanceof NormalItemHolder) {
            //注意括号
            bindNormalItem(story, ((NormalItemHolder) holder).title, ((NormalItemHolder) holder).image);
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && !data.get(position).getData().equals("NO_DATE")) {
            return HEADER_ITEM;
        } else if(position == 0 && data.get(position).getData().equals("NO_DATE")){
            return NORMAL_ITEM;
        }
        return data.get(position - 1).getData().equals(data.get(position).getData()) ? NORMAL_ITEM : HEADER_ITEM;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    private class NormalItemHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        LinearLayout linearLayout;

        public NormalItemHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_title);
            image = (ImageView) itemView.findViewById(R.id.item_icon);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.item_container);
        }
    }

    private class HeadItemHolder extends RecyclerView.ViewHolder {
        TextView newsTime;
        CardView cardView;
        LinearLayout linearLayout;
        TextView title;
        ImageView image;


        public HeadItemHolder(View itemView) {
            super(itemView);
            newsTime = (TextView) itemView.findViewById(R.id.item_time);
            title = (TextView) itemView.findViewById(R.id.item_title);
            image = (ImageView) itemView.findViewById(R.id.item_image);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.item_container);
            cardView = (CardView) itemView.findViewById(R.id.head_cardView);
        }
    }

    private void bindNormalItem(NewsBean.StoriesBean story, TextView textView, ImageView imageView) {
        textView.setText(story.getTitle());
        if (story.getImages() != null) {
            ActivityUtil.handleImageByGlide(mContext, story.getImages().get(0), imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    private void bindHeadItem(NewsBean.StoriesBean story, HeadItemHolder headItemHolder) {
        bindNormalItem(story, headItemHolder.title, headItemHolder.image);
        headItemHolder.newsTime.setText(story.getWeekend());
    }
}
