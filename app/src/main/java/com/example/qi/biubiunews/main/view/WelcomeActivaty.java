package com.example.qi.biubiunews.main.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.callback.HttpCallback;
import com.example.qi.biubiunews.models.Category;
import com.example.qi.biubiunews.models.Site;
import com.example.qi.biubiunews.utils.DbUtils;
import com.example.qi.biubiunews.utils.HttpUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class WelcomeActivaty extends AppCompatActivity {
    private ImageView imageView;
    private HttpUtils httpUtils;
    private DbUtils dbUtils;
    private boolean LOADDATA = false;
    private boolean LOAD_FINISH = false;
    private boolean ANIM_END;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_activaty);

        init();
        loadAnim();
    }

    private void init() {
        httpUtils = new HttpUtils(this);
        imageView = (ImageView) findViewById(R.id.welcome_image);
    }

    private void loadAnim() {
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int heigth = manager.getDefaultDisplay().getHeight();
        Picasso.with(imageView.getContext()).load(R.drawable.image)
                .resize(width,heigth)
                .centerCrop()
                .into(imageView);

        ScaleAnimation animation = new ScaleAnimation(1.0f,1.1f,1.0f,1.1f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(3000);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ANIM_END = false;
                File dbFile = getDatabasePath("news");
                if(!dbFile.exists()){
                    LOADDATA = true;
                    loadData();
                }

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                ANIM_END = true;
                if((LOADDATA && LOAD_FINISH) || !LOADDATA){
                    startActivity(new Intent(WelcomeActivaty.this,MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animation);
    }





    private void loadData() {

        dbUtils = new DbUtils(this);
        httpUtils.get_site_list(new HttpCallback() {
        @Override
            public void onResponse(Response response) {
                List<Site> sites = (List<Site>) response.body();
                Log.i("sites",sites.toString());
                dbUtils.insert_sites(sites);
                loadCategory();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(WelcomeActivaty.this, "暂时无法连接", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loadCategory() {
        httpUtils.get_category_list(new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                List<Category> categorys = (List<Category>) response.body();
                Log.i("sites",categorys.toString());
                dbUtils.insert_category(categorys);
                LOAD_FINISH = true;
                if (ANIM_END){
                    startActivity(new Intent(WelcomeActivaty.this,MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(WelcomeActivaty.this, "暂时无法连接", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
