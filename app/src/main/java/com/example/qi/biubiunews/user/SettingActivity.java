package com.example.qi.biubiunews.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qi.biubiunews.BiuApplication;
import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.main.view.MainActivity;
import com.example.qi.biubiunews.models.Site;
import com.example.qi.biubiunews.models.User;
import com.example.qi.biubiunews.utils.DbUtils;
import com.example.qi.biubiunews.utils.Utils;

import java.util.List;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private BiuApplication app;
    private LinearLayout click_item_site;
    private LinearLayout click_item_phone;
    private LinearLayout click_item_password;
    private TextView tv_site;
    private TextView tv_phone;
    private Site site;
    private User user;
    private List<Site> sites;
    private Toolbar toolbar;
    private DbUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();

    }

    private void init() {
        app = (BiuApplication) getApplication();
        click_item_site = (LinearLayout) findViewById(R.id.setting_item_site);
        click_item_phone = (LinearLayout) findViewById(R.id.setting_item_phone);
        click_item_password = (LinearLayout) findViewById(R.id.setting_item_password);
        tv_site = (TextView) findViewById(R.id.setting_tv_site);
        tv_phone = (TextView) findViewById(R.id.setting_tv_phone);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        site = Utils.getSite(this);
        user = Utils.getUser(this);
        tv_site.setText(site.getName());
        tv_phone.setText(user.getPhone());
        click_item_site.setOnClickListener(this);
        click_item_phone.setOnClickListener(this);
        click_item_password.setOnClickListener(this);
        dbUtils = new DbUtils(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("设置");
    }


    private void selectSite() {
        sites = dbUtils.get_sites();
        String[] site_list = new String[sites.size()];
        for (Site s : sites){
            int index = sites.indexOf(s);
            site_list[index] = s.getName();
        }
        AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this,R.style.Theme_AppCompat_Light_Dialog)
                .setItems(site_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        site = sites.get(which);
                        Utils.setSite(SettingActivity.this,site);
                        Toast.makeText(SettingActivity.this, "已切换新闻源为" + site.getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.setting_item_site:
                selectSite();
                break;
            case R.id.setting_item_phone:
                Toast.makeText(SettingActivity.this, "暂未实现修改绑定手机", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_item_password:
                Intent intent = new Intent(this,ResetActivity.class);
                intent.putExtra("phone",user.getPhone());
                startActivity(intent);
                break;


        }
    }


}
