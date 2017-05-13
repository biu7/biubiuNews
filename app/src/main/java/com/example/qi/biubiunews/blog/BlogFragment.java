package com.example.qi.biubiunews.blog;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.example.qi.biubiunews.news.EditNewsActivity;
import com.example.qi.biubiunews.news.NewsDetailActivity;
import com.example.qi.biubiunews.news.NewsListFragment;
import com.example.qi.biubiunews.user.adapter.UserNewsRecyclerAdapter;
import com.example.qi.biubiunews.utils.HttpUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlogFragment extends Fragment {

 /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 10000;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 20;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;

    private static int FLAG_NEW = 0;


    private LRecyclerView recyclerView;
    private FloatingActionButton actionButton;
    private UserNewsRecyclerAdapter recyclerAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private HttpUtils httpUtils;

    public static BlogFragment newInstance() {
        BlogFragment fragment = new BlogFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_blog, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("TimeLine");
        httpUtils = new HttpUtils(getActivity());
        actionButton = (FloatingActionButton) root.findViewById(R.id.new_news);
        recyclerView = (LRecyclerView) root.findViewById(R.id.timeline_recyl);
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
                if (TextUtils.isEmpty(((MainActivity) getActivity()).getToken().getToken())){
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    recyclerView.refreshComplete(REQUEST_COUNT);
                    return;
                }else{
                    recyclerAdapter.clear();
                    recyclerAdapter.notifyDataSetChanged();
                    requestData(1);

                }
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
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("news_id",item.getId());
                    startActivity(intent);
                }
            }

        });
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditNewsActivity.class);
                intent.setFlags(FLAG_NEW);
                startActivity(intent);
            }
        });
        return root;
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
        httpUtils.get_timeline(page, new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                if(response.code() != 200){
                    Toast.makeText(getActivity(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    return;
                }
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
