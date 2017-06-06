package com.example.qi.biubiunews.user.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.example.qi.biubiunews.ListBaseAdapter;
import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.SuperViewHolder;
import com.example.qi.biubiunews.models.Package;

/**
 * Created by qi on 17-5-18.
 */

public class PackageRecyclerAdapter extends ListBaseAdapter {
    public PackageRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.recycler_item_package;
    }


    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        Package mPackage = (Package) mDataList.get(position);
        TextView tv_name = holder.getView(R.id.package_name);
        TextView tv_count = holder.getView(R.id.news_count);
        Log.i("a",mPackage.toString());
        tv_name.setText(mPackage.getName());
        tv_count.setText(String.valueOf(mPackage.getCount()));
    }
}
