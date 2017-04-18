package com.example.helldefender.rvfunction.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.data.entity.NewsBean;
import com.example.helldefender.rvfunction.util.ImageManager;

import java.util.List;

/**
 * Created by Helldefender on 2016/11/24.
 */

public class EditorRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewsBean.EditorsBean> data;
    private Context context;

    public EditorRvAdapter(Context context, List<NewsBean.EditorsBean> data) {
        this.context = context;
        this.data = data;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditImageView(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_editor, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        NewsBean.EditorsBean editorsBean = data.get(position);
        ImageManager.handleImageToRound(context, data.get(position).getAvatar(), ((EditImageView) holder).editorImage, 20, 20, 20);
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class EditImageView extends RecyclerView.ViewHolder {
        ImageView editorImage;

        public EditImageView(View itemView) {
            super(itemView);
            editorImage = (ImageView) itemView.findViewById(R.id.editor_item_image);
        }
    }
}
