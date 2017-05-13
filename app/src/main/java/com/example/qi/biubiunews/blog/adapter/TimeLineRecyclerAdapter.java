package com.example.qi.biubiunews.blog.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qi.biubiunews.ListBaseAdapter;
import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.SuperViewHolder;
import com.example.qi.biubiunews.models.News;

/**
 * Created by qi on 17-5-8.
 */

public class TimeLineRecyclerAdapter extends ListBaseAdapter {
    public TimeLineRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.recycler_item_news;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        News news = (News) mDataList.get(position);
        ImageView img = holder.getView(R.id.news_icon);
        TextView tv_title = holder.getView(R.id.news_title);
        TextView tv_time = holder.getView(R.id.news_time);

        tv_title.setText(news.getTitle());
        tv_time.setText(news.getTimestamp());

    }

}
