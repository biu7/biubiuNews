package com.example.qi.biubiunews.user;

import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.callback.HttpCallback;
import com.example.qi.biubiunews.models.News;
import com.example.qi.biubiunews.news.NewsDetailActivity;
import com.example.qi.biubiunews.user.adapter.UserNewsRecyclerAdapter;
import com.example.qi.biubiunews.utils.HttpUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class UserNewsActivity extends AppCompatActivity{

     /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 10000;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 20;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;



    private LRecyclerView recyclerView;
    private UserNewsRecyclerAdapter recyclerAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private HttpUtils httpUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_news);
        httpUtils = new HttpUtils(this);

        recyclerView = (LRecyclerView) findViewById(R.id.user_news_recyl);
        recyclerAdapter = new UserNewsRecyclerAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(recyclerAdapter);
        recyclerView.setAdapter(mLRecyclerViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        recyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {


                requestData(1);

            }
        });

        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (recyclerAdapter.getItemCount() % 20 == 0){
                    int page = recyclerAdapter.getItemCount()/20 + 2;
                    requestData(page);
                }
                 else {
                    //the end
                    recyclerView.setNoMore(true);
                }
            }
        });
        recyclerView.refresh();

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (recyclerAdapter.getDataList().size() > position) {
                    News item = (News) recyclerAdapter.getDataList().get(position);
                    Intent intent = new Intent(UserNewsActivity.this, NewsDetailActivity.class);
                    intent.putExtra("news_id",item.getId());
                    startActivity(intent);

                }
            }

        });

    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(List<News> list) {
        recyclerAdapter.addAll(list);
        mCurrentCounter += list.size();

    }

    private void requestData(int page) {
        //请求网络
        httpUtils.get_self_news(page,new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                List<News> list = (List<News>) response.body();
                Log.i("news",list.toString());
                recyclerAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                UserNewsActivity.this.addItems(list);
                recyclerView.refreshComplete(REQUEST_COUNT);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


}
