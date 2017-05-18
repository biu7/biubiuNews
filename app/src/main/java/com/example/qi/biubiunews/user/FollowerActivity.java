package com.example.qi.biubiunews.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.callback.HttpCallback;
import com.example.qi.biubiunews.models.News;
import com.example.qi.biubiunews.models.User;
import com.example.qi.biubiunews.user.adapter.UserFollowAdapter;
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

public class FollowerActivity extends AppCompatActivity {

     /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 10000;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 20;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;

    private int FLAG;

    private List<User> list;

    private Toolbar toolbar;

    private LRecyclerView recyclerView;
    private UserFollowAdapter recyclerAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private HttpUtils httpUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        FLAG = getIntent().getFlags();
        httpUtils = new HttpUtils(this);
        list = new ArrayList<>();
        recyclerView = (LRecyclerView) findViewById(R.id.user_follow_recyl);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerAdapter = new UserFollowAdapter(this);
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
                Intent intent = new Intent(FollowerActivity.this,UserActivity.class);
                intent.putExtra("user",list.get(position));
                startActivity(intent);
            }

        });

    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(List<User> list) {
        recyclerAdapter.addAll(list);
        mCurrentCounter += list.size();

    }

    private void requestData(int page) {
        switch (FLAG){
            case 0:
                getSupportActionBar().setTitle("关注");
                loadFollowed();
                break;
            case 1:
                getSupportActionBar().setTitle("粉丝");
                loadFollower();
                break;
            case 2:
                getSupportActionBar().setTitle("关注");
                loadUserFollowed();
                break;
            case 3:
                loadUserFollower();
                getSupportActionBar().setTitle("粉丝");
                break;
        }
    }



    private void loadFollower(){
        //请求网络
        httpUtils.get_self_follower(new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                list = (List<User>) response.body();
                recyclerAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                FollowerActivity.this.addItems(list);
                recyclerView.refreshComplete(REQUEST_COUNT);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
    private void loadFollowed(){
        //请求网络
        httpUtils.get_self_followed(new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                list = (List<User>) response.body();
                recyclerAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                FollowerActivity.this.addItems(list);
                recyclerView.refreshComplete(REQUEST_COUNT);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(FollowerActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserFollower() {
        int user_id = getIntent().getIntExtra("user_id",1);
        httpUtils.get_user_follower(user_id, new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                list = (List<User>) response.body();
                recyclerAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                FollowerActivity.this.addItems(list);
                recyclerView.refreshComplete(REQUEST_COUNT);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(FollowerActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadUserFollowed() {
        int user_id = getIntent().getIntExtra("user_id",1);
        httpUtils.get_user_followed(user_id, new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                list = (List<User>) response.body();
                recyclerAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                FollowerActivity.this.addItems(list);
                recyclerView.refreshComplete(REQUEST_COUNT);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(FollowerActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
