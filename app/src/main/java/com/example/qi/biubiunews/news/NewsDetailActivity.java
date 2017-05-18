package com.example.qi.biubiunews.news;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.callback.HttpCallback;
import com.example.qi.biubiunews.models.Comment;
import com.example.qi.biubiunews.models.News;
import com.example.qi.biubiunews.user.UserNewsActivity;
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

public class NewsDetailActivity extends AppCompatActivity {


     /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 10000;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 20;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;

    private WebView webView;
    private Toolbar toolbar;
    private LRecyclerView recyclerView;
    private EditText et_comment;
    private Button btn_comment;
    private int news_id;
    private HttpUtils httpUtils;
    private CommentRecyclerAdapter recyclerAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        news_id = getIntent().getIntExtra("news_id",0);

        init();
        getNews();

    }

    private void init() {
        httpUtils = new HttpUtils(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        webView = (WebView) findViewById(R.id.webview);
        et_comment = (EditText) findViewById(R.id.et_comment);
        btn_comment = (Button) findViewById(R.id.btn_comment);

        recyclerView = (LRecyclerView) findViewById(R.id.recycler_comment);
        recyclerAdapter = new CommentRecyclerAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(recyclerAdapter);
        recyclerView.setAdapter(mLRecyclerViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        recyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setPullRefreshEnabled(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                requestData();
            }
        });

        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });

        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment comment = new Comment();
                comment.setNews_id(news_id);
                String content = et_comment.getText().toString();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(NewsDetailActivity.this, "请填写评论内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                comment.setContent(content);
                httpUtils.new_comment(comment, new HttpCallback() {
                    @Override
                    public void onResponse(Response response) {
                        Toast.makeText(NewsDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                        et_comment.setText("");
                        requestData();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(NewsDetailActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void getNews() {
            httpUtils.get_news_info(news_id, new HttpCallback() {
                @Override
                public void onResponse(Response response) {
                    News news = (News) response.body();
                    webView.loadData(news.getContent(),"text/html; charset=UTF-8",null);
                    toolbar.setTitle(news.getTitle());
                    setSupportActionBar(toolbar);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(List<Comment> list) {
        recyclerAdapter.addAll(list);
        mCurrentCounter += list.size();

    }

    private void requestData() {
        //请求网络
        httpUtils.get_comment_list(news_id,new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                List<Comment> list = (List<Comment>) response.body();
                recyclerAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                addItems(list);
                recyclerView.refreshComplete(REQUEST_COUNT);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(NewsDetailActivity.this, t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }




}
