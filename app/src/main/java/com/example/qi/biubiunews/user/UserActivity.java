package com.example.qi.biubiunews.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.callback.HttpCallback;
import com.example.qi.biubiunews.main.view.MainActivity;
import com.example.qi.biubiunews.models.User;
import com.example.qi.biubiunews.utils.CircleTransform;
import com.example.qi.biubiunews.utils.HttpUtils;
import com.squareup.picasso.Picasso;

import retrofit2.Response;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {


    private LinearLayout click_followed;
    private LinearLayout click_follower;
    private LinearLayout click_news;
    private LinearLayout click_package;
    private Button click_follow;

    private TextView tv_location;
    private TextView tv_about_me;

    private ImageView icon;
    private Toolbar toolbar;

    private User user;
    private HttpUtils httpUtils;
    private boolean is_followed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        user = getIntent().getParcelableExtra("user");
        init();
        setUser();
    }

    private void setUser() {
        tv_location.setText(user.getLocation());
        tv_about_me.setText(user.getAbout_me());
        getSupportActionBar().setTitle(user.getName());
        if (!TextUtils.isEmpty(user.getIcon())){
            Picasso.with(this)
                .load(user.getIcon())
                .transform(new CircleTransform())
                .into(icon);
        }
        is_follow();
    }

    private void init() {
        httpUtils = new HttpUtils(this);
        click_followed = (LinearLayout) findViewById(R.id.click_user_followed);
        click_follower = (LinearLayout) findViewById(R.id.click_user_follower);
        click_news = (LinearLayout) findViewById(R.id.click_user_news);
        click_package = (LinearLayout) findViewById(R.id.click_user_packages);
        click_follow = (Button) findViewById(R.id.btn_follow);


        tv_about_me = (TextView) findViewById(R.id.tv_user_about_me);
        tv_location = (TextView) findViewById(R.id.tv_user_location);

        icon = (ImageView) findViewById(R.id.image_user_icon);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        click_followed.setOnClickListener(this);
        click_follower.setOnClickListener(this);
        click_news.setOnClickListener(this);
        click_package.setOnClickListener(this);
        click_follow.setOnClickListener(this);

    }
    public void is_follow(){
        httpUtils.is_follow(user.getId(), new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                is_followed = (boolean) response.body();
                if(is_followed){
                    click_follow.setText("已关注");
                }
                else{
                    click_follow.setText("关注");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(UserActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.click_user_followed:
                Intent intent = new Intent(this,FollowerActivity.class);
                intent.setFlags(2);
                intent.putExtra("user_id",user.getId());
                startActivity(intent);
                break;
            case R.id.click_user_follower:
                Intent intent1 = new Intent(this,FollowerActivity.class);
                intent1.setFlags(3);
                intent1.putExtra("user_id",user.getId());
                startActivity(intent1);
                break;
            case R.id.click_user_news:
                Intent intent2 = new Intent(this,UserNewsActivity.class);
                intent2.setFlags(1);
                intent2.putExtra("user_id",user.getId());
                startActivity(intent2);
                break;
            case R.id.click_user_packages:
                Intent intent3 = new Intent(UserActivity.this,PackageActivity.class);
                intent3.setFlags(1);
                intent3.putExtra("user_id",user.getId());
                startActivity(intent3);
                break;
            case R.id.btn_follow:
                if(is_followed){
                    unFollowUser(user.getId());

                }else{
                    followUser(user.getId());
                }
            break;
        }
    }

    private void unFollowUser(int id) {
        httpUtils.unfollow(id, new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                if (response.code() == 201) {
                    Toast.makeText(UserActivity.this, "取关成功", Toast.LENGTH_SHORT).show();
                    is_follow();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(UserActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void followUser(int id) {
        httpUtils.follow(id, new HttpCallback() {
            @Override
            public void onResponse(Response response) {

                if (response.code() == 201){
                    Toast.makeText(UserActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                    is_follow();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(UserActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
