package com.example.qi.biubiunews.news;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.callback.HttpCallback;
import com.example.qi.biubiunews.main.view.MainActivity;
import com.example.qi.biubiunews.models.News;
import com.example.qi.biubiunews.models.Token;
import com.example.qi.biubiunews.user.adapter.UserNewsRecyclerAdapter;
import com.example.qi.biubiunews.utils.HttpUtils;
import com.example.qi.biubiunews.utils.Utils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.List;

import retrofit2.Response;

/**
 * Created by qi on 2016/11/22.
 */

public class NewsListFragment extends android.support.v4.app.Fragment  {
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
    private static final String CATAGORY = "section_number";
    private int cat_id;
    private Token token;
    public NewsListFragment() {
    }

    public static NewsListFragment newInstance(int sectionNumber) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putInt(CATAGORY, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newslist, container, false);

        cat_id = getArguments().getInt(CATAGORY);
        token = ((MainActivity) getActivity()).getToken();
        httpUtils = new HttpUtils(getActivity());
        recyclerView = (LRecyclerView) rootView.findViewById(R.id.user_news_recyl);
        recyclerAdapter = new UserNewsRecyclerAdapter(getActivity());

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(recyclerAdapter);

        recyclerView.setAdapter(mLRecyclerViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                    recyclerView.setNoMore(true);
                }
            }
        });
        recyclerView.refresh();


        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (recyclerAdapter.getDataList().size() > position) {
                    if(TextUtils.isEmpty(token.getToken())){
                        Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    }else{
                        News item = (News) recyclerAdapter.getDataList().get(position);
                        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                        intent.putExtra("news_id",item.getId());
                        startActivity(intent);
                    }

                }
            }

        });
        return rootView;
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
        httpUtils.get_news_list(cat_id, page, new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                List<News> list = (List<News>) response.body();
                mCurrentCounter = 0;
                addItems(list);
                recyclerView.refreshComplete(REQUEST_COUNT);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}



