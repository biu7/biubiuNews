package com.example.qi.biubiunews.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.callback.HttpCallback;
import com.example.qi.biubiunews.models.News;
import com.example.qi.biubiunews.models.Package;
import com.example.qi.biubiunews.news.NewsDetailActivity;
import com.example.qi.biubiunews.user.adapter.UserNewsRecyclerAdapter;
import com.example.qi.biubiunews.utils.HttpUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
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

    private int FLAG;



    private LRecyclerView recyclerView;
    private UserNewsRecyclerAdapter recyclerAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private HttpUtils httpUtils;

    private Toolbar toolbar;
    private Package mPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_news);
        FLAG = getIntent().getFlags();
        httpUtils = new HttpUtils(this);

        recyclerView = (LRecyclerView) findViewById(R.id.user_news_recyl);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        if (FLAG == 2){
            mLRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, int position) {
                    new AlertDialog.Builder(UserNewsActivity.this)
                            .setItems(new String[]{"取消收藏"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    News news = (News) recyclerAdapter.getDataList().get(which);
                                    httpUtils.unCollection_news(mPackage.getId(), news.getId(), new HttpCallback() {
                                        @Override
                                        public void onResponse(Response response) {
                                            Toast.makeText(UserNewsActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                                            recyclerView.refresh();
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            Toast.makeText(UserNewsActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).show();
                }
            });
        }

    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(List<News> list) {
        recyclerAdapter.addAll(list);
        mCurrentCounter += list.size();

    }

    private void requestData(int page) {

        switch(FLAG){
            case 0:
                loadNews(page);
                break;
            case 1:
                loadUserNew(page);
                break;
            case 2:
                loadCollection();
                break;
        }

    }

    private void loadCollection() {

        mPackage  = getIntent().getParcelableExtra("package");
        getSupportActionBar().setTitle(mPackage.getName());
        httpUtils.get_package_info(mPackage.getId(), new HttpCallback() {
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

    private void loadUserNew(int page) {
        int user_id = getIntent().getIntExtra("user_id",1);
        httpUtils.get_user_news(user_id, page,new HttpCallback() {
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
                Toast.makeText(UserNewsActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadNews(int page) {
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
                Toast.makeText(UserNewsActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
