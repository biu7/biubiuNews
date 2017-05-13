package com.example.qi.biubiunews.user.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qi.biubiunews.ListBaseAdapter;
import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.SuperViewHolder;
import com.example.qi.biubiunews.models.User;
import com.example.qi.biubiunews.utils.CircleTransform;
import com.squareup.picasso.Picasso;

/**
 * Created by qi on 17-5-8.
 */

public class UserFollowAdapter extends ListBaseAdapter {
    public UserFollowAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.recycler_item_follow;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        User user = (User) mDataList.get(position);

        ImageView icon = holder.getView(R.id.recycler_user_icon);
        TextView tv_name = holder.getView(R.id.recycler_user_name);
        TextView tv_about = holder.getView(R.id.recycler_user_about);
        TextView tv_location = holder.getView(R.id.recycler_user_location);

        tv_name.setText(user.getName());
        tv_about.setText(user.getAbout_me());
        tv_location.setText(user.getLocation());
        Picasso.with(mContext)
                .load(user.getIcon())
                .transform(new CircleTransform())
                .into(icon);
    }
}
