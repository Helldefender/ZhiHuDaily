package com.example.helldefender.rvfunction.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.data.entity.NewsCommentsBean;
import com.example.helldefender.rvfunction.util.ImageManager;

import java.util.List;

/**
 * Created by Helldefender on 2016/11/28.
 */

public class CommentsRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private int mComments;
    private List<NewsCommentsBean.CommentsBean> shortData;
    private List<NewsCommentsBean.CommentsBean> longData;
    private static final int COMMENTS_ITEM = 0;
    private static final int SPECIAL_ITEM = 1;

    public CommentsRvAdapter(Context mContext, List<NewsCommentsBean.CommentsBean> shortData, List<NewsCommentsBean.CommentsBean> longData, int mComments) {
        this.mContext = mContext;
        this.shortData = shortData;
        this.longData = longData;
        this.mComments = mComments;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == COMMENTS_ITEM) {
            return new CommentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments, parent, false));
        } else{
            return new SpecialHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments_num, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (position < longData.size()) {
            NewsCommentsBean.CommentsBean commentsBean = longData.get(position);
            bindCommentItem(commentsBean, (CommentsHolder) holder);
            handleClickEvent(holder);
        } else if (position == longData.size()) {
            bindSpecItem((SpecialHolder) holder);
            handleClickEvent(holder);
        } else {
            NewsCommentsBean.CommentsBean commentsBean = shortData.get(position - longData.size() - 1);
            bindCommentItem(commentsBean, (CommentsHolder) holder);
            handleClickEvent(holder);
        }
    }

    @Override
    public int getItemCount() {
        return longData.size() + shortData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == longData.size())
            return SPECIAL_ITEM;
        return COMMENTS_ITEM;
    }

    private class CommentsHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        ImageView popularityBtn;
        TextView author;
        TextView popularity;
        TextView content;
        TextView time;

        public CommentsHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.comment_avatar);
            popularity = (TextView) itemView.findViewById(R.id.comment_popularity_quantity);
            popularityBtn = (ImageView) itemView.findViewById(R.id.comment_popularity_icon);
            author = (TextView) itemView.findViewById(R.id.comment_author);
            time = (TextView) itemView.findViewById(R.id.comment_time);
            content = (TextView) itemView.findViewById(R.id.comment_content);
        }
    }

    private class SpecialHolder extends RecyclerView.ViewHolder {
        TextView shortComments;
        ImageButton loadMore;

        public SpecialHolder(View itemView) {
            super(itemView);
            shortComments = (TextView) itemView.findViewById(R.id.item_comment_spec_text);
            loadMore = (ImageButton) itemView.findViewById(R.id.item_comment_spec_btn);
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    private void bindCommentItem(NewsCommentsBean.CommentsBean commentsBean, CommentsHolder commentsholder) {
        commentsholder.content.setText(commentsBean.getContent());
        commentsholder.popularity.setText(commentsBean.getLikes() + "");
        commentsholder.author.setText(commentsBean.getAuthor());
        commentsholder.time.setText(commentsBean.getTime() + "");
        ImageManager.handleImageToRound(mContext, commentsBean.getAvatar(), commentsholder.avatar, 60, 60, 60);
    }

    private void bindSpecItem(SpecialHolder specialHolder) {
        specialHolder.shortComments.setText((mComments - longData.size()) + "条短评论");
    }

    private void handleClickEvent(final RecyclerView.ViewHolder holder) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClickListener(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
    }

}
