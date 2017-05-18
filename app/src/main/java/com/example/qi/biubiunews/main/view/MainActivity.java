package com.example.qi.biubiunews.main.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;


import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.callback.HttpCallback;
import com.example.qi.biubiunews.models.Category;
import com.example.qi.biubiunews.models.Token;

import com.example.qi.biubiunews.models.User;
import com.example.qi.biubiunews.news.NewsFragment;
import com.example.qi.biubiunews.utils.HttpUtils;
import com.example.qi.biubiunews.utils.Utils;

import java.io.File;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private MyNanigationViewAdapter myNavigationViewAdapter;
    private Token token;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        myNavigationViewAdapter = new MyNanigationViewAdapter(getSupportFragmentManager(),R.id.container,R.id.navigation_news,savedInstanceState);
        myNavigationViewAdapter.attachTo(bottomNavigationView);
        myNavigationViewAdapter.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        myNavigationViewAdapter.onSaveInstanceState(outState);
    }

    private void init() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("asdasd");
        token = Utils.loadToken(this);
        user = Utils.getUser(this);

    }
    public Token getToken(){
        return token;
    }

    public User getUser(){
        return user;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        token = Utils.loadToken(this);
    }



}
