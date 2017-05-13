package com.example.qi.biubiunews.news;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.commonsware.cwac.anddown.AndDown;
import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.callback.HttpCallback;
import com.example.qi.biubiunews.models.News;
import com.example.qi.biubiunews.utils.HttpUtils;

import retrofit2.Response;

public class EditNewsActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private AndDown andDown;
    private EditText et_title;
    private EditText et_content;
    private Toolbar toolbar;
    private FloatingActionButton actionButton;
    private WebView webView;
    private HttpUtils httpUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        et_title = (EditText) findViewById(R.id.edit_et_title);
        et_content = (EditText) findViewById(R.id.edit_et_content);
        webView = (WebView) findViewById(R.id.webview);
        httpUtils = new HttpUtils(this);
        andDown = new AndDown();
        actionButton = (FloatingActionButton) findViewById(R.id.edit_commit);
        toolbar.setTitle("Title");
        setSupportActionBar(toolbar);
        actionButton.setOnClickListener(this);
        et_content.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        String news_title = et_title.getText().toString();
        String news_content = et_content.getText().toString();
        if (TextUtils.isEmpty(news_title) || TextUtils.isEmpty(news_content)){
            Toast.makeText(this, "标题和内容不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        News news = new News();
        news.setTitle(news_title);
        news.setContent(news_content);
        httpUtils.new_news(news, new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                if (response.code() == 201){
                    News new_news = (News) response.body();
                    Intent intent = new Intent(EditNewsActivity.this,NewsDetailActivity.class);
                    intent.putExtra("news_id",new_news.getId());
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String html_content = andDown.markdownToHtml(s.toString());
        webView.loadData(html_content,"text/html; charset=UTF-8",null);

    }
}
