package com.example.q.pocketmusic.module.home.ask.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.q.pocketmusic.R;
import com.example.q.pocketmusic.model.bean.ask.AskSongPost;

import com.example.q.pocketmusic.util.DisplayStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;



public class HomeAskListAdapter extends RecyclerArrayAdapter<AskSongPost> {
    private DisplayStrategy displayStrategy;
    private Context context;
    private OnItemClickListener listener;


    public HomeAskListAdapter(Context context) {
        super(context);
        this.context = context;
        displayStrategy = new DisplayStrategy();
    }

    public void setListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClickHeadIv(int position);

        void onClickContent(int position);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(parent);
    }

    class MyViewHolder extends BaseViewHolder<AskSongPost> {
        TextView postUserTitleTv;
        TextView postUserContentTv;
        TextView postUserNameTv;
        TextView postUserDateTv;
        TextView postUserCommentNumTv;

        ImageView postUserHeadIv;
        LinearLayout contentLl;

        public MyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_ask_song);
            postUserTitleTv = $(R.id.post_user_title_tv);
            postUserContentTv = $(R.id.post_user_content_tv);
            postUserNameTv = $(R.id.post_user_name_tv);
            postUserHeadIv = $(R.id.post_user_head_iv);
            postUserDateTv = $(R.id.post_user_date_tv);
            postUserCommentNumTv = $(R.id.post_user_comment_num_tv);
            contentLl = $(R.id.content_ll);
            //????????????
            contentLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickContent(getAdapterPosition());
                    }
                }
            });
            //????????????
            postUserHeadIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickHeadIv(getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public void setData(AskSongPost data) {
            super.setData(data);
            postUserTitleTv.setText("?????????" + data.getTitle());
            postUserContentTv.setText("?????????" + data.getContent());
            postUserNameTv.setText(data.getUser().getNickName());
            displayStrategy.displayCircle(context, data.getUser().getHeadImg(), postUserHeadIv);
            postUserDateTv.setText(data.getCreatedAt());
            postUserCommentNumTv.setText(String.valueOf(data.getCommentNum()));
        }

    }
}
