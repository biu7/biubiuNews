package com.example.qi.biubiunews.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qi.biubiunews.ListBaseAdapter;
import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.SuperViewHolder;
import com.example.qi.biubiunews.models.News;
import com.example.qi.biubiunews.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by qi on 17-5-8.
 */

public class UserNewsRecyclerAdapter extends ListBaseAdapter {
    public UserNewsRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.recycler_item_news;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        News news = (News) mDataList.get(position);
        ImageView icon = holder.getView(R.id.news_icon);
        TextView tv_title = holder.getView(R.id.news_title);
        TextView tv_time = holder.getView(R.id.news_time);

        tv_title.setText(news.getTitle());
        tv_time.setText(news.getTimestamp());
        Picasso.with(mContext)
                .load(R.drawable.icon_default)
                .error(R.drawable.icon_default)
                .transform(new CircleTransform())
                .into(icon);

    }

}
