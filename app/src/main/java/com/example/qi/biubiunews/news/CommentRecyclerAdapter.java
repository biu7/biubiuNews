package com.example.qi.biubiunews.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.qi.biubiunews.ListBaseAdapter;
import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.SuperViewHolder;
import com.example.qi.biubiunews.models.Comment;

/**
 * Created by qi on 17-5-10.
 */

public class CommentRecyclerAdapter extends ListBaseAdapter {

    public CommentRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.recycler_item_comment;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        Comment comment = (Comment) mDataList.get(position);
        TextView tv_comment = holder.getView(R.id.tv_comment_comment);
        TextView tv_author = holder.getView(R.id.tv_comment_author);
        TextView tv_time = holder.getView(R.id.tv_comment_time);

        tv_comment.setText(comment.getContent());
        tv_author.setText(comment.getUser().getName());
        tv_time.setText(comment.getTime());

    }

}
