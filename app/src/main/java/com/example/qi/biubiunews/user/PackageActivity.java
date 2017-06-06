package com.example.qi.biubiunews.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.callback.HttpCallback;
import com.example.qi.biubiunews.models.Package;
import com.example.qi.biubiunews.user.adapter.PackageRecyclerAdapter;
import com.example.qi.biubiunews.utils.HttpUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import retrofit2.Response;

public class PackageActivity extends AppCompatActivity implements View.OnClickListener {

    /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 10000;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 20;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;

    private int FLAG;



    private LRecyclerView recyclerView;
    private PackageRecyclerAdapter recyclerAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private HttpUtils httpUtils;
    private Toolbar toolbar;
    private List<Package> list;
    private FloatingActionButton click_new_package;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        FLAG = getIntent().getFlags();
        httpUtils = new HttpUtils(this);
        list = new ArrayList<>();
        recyclerView = (LRecyclerView) findViewById(R.id.package_recyl);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        click_new_package = (FloatingActionButton) findViewById(R.id.click_new_package);
        click_new_package.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("收藏夹");

        recyclerAdapter = new PackageRecyclerAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(recyclerAdapter);
        recyclerView.setAdapter(mLRecyclerViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        recyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });

        recyclerView.refresh();

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (recyclerAdapter.getDataList().size() > position) {
                    Package mPackage = (Package) recyclerAdapter.getDataList().get(position);
                    Intent intent = new Intent(PackageActivity.this, UserNewsActivity.class);
                    intent.setFlags(2);;
                    intent.putExtra("package",mPackage);
                    startActivity(intent);

                }
            }

        });

        if (FLAG == 0){
            mLRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                final Package mPackage = (Package) recyclerAdapter.getDataList().get(position);
                new AlertDialog.Builder(PackageActivity.this)
                        .setItems(new String[]{"删除"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                httpUtils.delete_package(mPackage.getId(), new HttpCallback() {
                                    @Override
                                    public void onResponse(Response response) {

                                        Toast.makeText(PackageActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                        recyclerView.refresh();
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Toast.makeText(PackageActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).show();
            }
        });
        }

    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(List<Package> list) {
        recyclerAdapter.addAll(list);
        mCurrentCounter += list.size();

    }

    private void requestData() {

        switch(FLAG){
            case 0:

                loadPackage();
                break;
            case 1:
                click_new_package.setVisibility(View.GONE);
                loadUserPackage();
                break;
        }

    }

    private void loadUserPackage() {
        int user_id = getIntent().getIntExtra("user_id",1);
        httpUtils.get_user_packages(user_id, new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                list = (List<Package>) response.body();
                recyclerAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                PackageActivity.this.addItems(list);
                recyclerView.refreshComplete(REQUEST_COUNT);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(PackageActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void loadPackage() {
         //请求网络
        httpUtils.get_self_packages(new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                list = (List<Package>) response.body();
                recyclerAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                PackageActivity.this.addItems(list);
                recyclerView.refreshComplete(REQUEST_COUNT);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(PackageActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.click_new_package:
                final EditText et_name = new EditText(this);
                new AlertDialog.Builder(PackageActivity.this)

                        .setView(et_name)
                        .setTitle("新建收藏夹")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = et_name.getText().toString();
                                if (TextUtils.isEmpty(name)) {
                                    Toast.makeText(PackageActivity.this, "收藏夹名不可为空", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                httpUtils.new_package(name, new HttpCallback() {
                                    @Override
                                    public void onResponse(Response response) {
                                        Toast.makeText(PackageActivity.this, "新建成功", Toast.LENGTH_SHORT).show();
                                        recyclerView.refresh();
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Log.i("asd",t.toString());

                                    }
                                });
                            }
                        })
                        .show();
                break;
        }
    }
}
