package com.example.qi.biubiunews.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.qi.biubiunews.BiuApplication;
import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.models.Site;
import com.example.qi.biubiunews.utils.DbUtils;
import java.util.List;


public class SettingActivity extends Activity implements View.OnClickListener {
    private BiuApplication app;
    private LinearLayout item_site;
    private TextView tv_site;
    private Button btn_logout;
    private Site site;

    private DbUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();

    }

    private void init() {
        app = (BiuApplication) getApplication();
        item_site = (LinearLayout) findViewById(R.id.setting_item_site);
        tv_site = (TextView) findViewById(R.id.setting_tv_site);
        btn_logout = (Button) findViewById(R.id.setting_btn_logout);
//        site = app.site;
        tv_site.setText(site.getName());
        item_site.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        dbUtils = new DbUtils(this);
    }

    private void save_setting(){
        SharedPreferences preferences = getSharedPreferences("setting",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("site_id",site.getId());
        editor.putString("site_name",site.getName());
        editor.commit();
    }

    private void selectSite() {
        final List<Site> sites = dbUtils.get_sites();
        String[] site_list = {};
        for (Site site : sites){
            int index = sites.indexOf(site);
            site_list[index] = site.getName();
        }
        AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this,R.style.Theme_AppCompat_Light_Dialog)
                .setItems(site_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        site = sites.get(which);
                        save_setting();
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
            case R.id.setting_btn_logout:

                break;

        }
    }


}
