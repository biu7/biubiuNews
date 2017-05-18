package com.example.qi.biubiunews.user;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.callback.HttpCallback;
import com.example.qi.biubiunews.main.view.MainActivity;
import com.example.qi.biubiunews.models.Site;
import com.example.qi.biubiunews.models.Token;
import com.example.qi.biubiunews.models.User;
import com.example.qi.biubiunews.utils.CircleTransform;
import com.example.qi.biubiunews.utils.DbUtils;
import com.example.qi.biubiunews.utils.HttpUtils;
import com.example.qi.biubiunews.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import retrofit2.Response;


public class UserFragment extends Fragment implements View.OnClickListener {
    private LinearLayout click_followed;
    private LinearLayout click_follower;
    private LinearLayout click_news;
    private LinearLayout click_login;
    private LinearLayout click_setting;
    private LinearLayout click_package;
    private ImageView click_edit;

    private TextView tv_location;
    private TextView tv_about_me;
    private TextView tv_login;

    private ImageView icon;

    private HttpUtils httpUtils;
    private User user;
    private Token token;


    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("请登录");
        initView(root);
        token = ((MainActivity) getActivity()).getToken();
        user = ((MainActivity) getActivity()).getUser();
        if (TextUtils.isEmpty(token.getToken())){
            return root;
        }
        tv_login.setText("退出登录");
        setUser(user);
        loadUser();
        return root;
    }

    private void loadUser() {
        httpUtils.get_self_profile(new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                user = (User) response.body();
                Utils.setUser(getActivity(),user);
                setUser(user);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("error",t.toString());
            }
        });
    }

    public void initView(View root){
        click_followed = (LinearLayout) root.findViewById(R.id.click_self_followed);
        click_follower = (LinearLayout) root.findViewById(R.id.click_self_follower);
        click_news = (LinearLayout) root.findViewById(R.id.click_self_news);
        click_login = (LinearLayout) root.findViewById(R.id.click_self_login);
        click_edit = (ImageView) root.findViewById(R.id.click_self_edit);
        click_package = (LinearLayout) root.findViewById(R.id.click_self_packages);
        click_setting = (LinearLayout) root.findViewById(R.id.click_self_setting);
        tv_location = (TextView) root.findViewById(R.id.tv_self_location);
        tv_about_me = (TextView) root.findViewById(R.id.tv_self_about_me);
        tv_login = (TextView) root.findViewById(R.id.tv_login);

        icon = (ImageView) root.findViewById(R.id.image_self_icon);

        icon.setOnClickListener(this);
        click_followed.setOnClickListener(this);
        click_follower.setOnClickListener(this);
        click_news.setOnClickListener(this);
        click_login.setOnClickListener(this);
        click_setting.setOnClickListener(this);
        click_edit.setOnClickListener(this);
        click_package.setOnClickListener(this);


        httpUtils = new HttpUtils(getActivity());

        Picasso.with(getActivity())
                .load(R.drawable.icon_default)
                .transform(new CircleTransform())
                .into(icon);
    }

    public void setUser(User user){
        tv_location.setText(user.getLocation());
        tv_about_me.setText(user.getAbout_me());
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(user.getName());
        if (!TextUtils.isEmpty(user.getIcon())){
            Picasso.with(getActivity())
                .load(user.getIcon())
                .transform(new CircleTransform())
                .into(icon);
        }

    }

    public void updateIcon(){
        new AlertDialog.Builder(getActivity())
                            .setItems(new String[]{"拍照","从手机中选择"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case 0:
                                            PhotoPicker.builder()
                                                .setOpenCamera(true)
                                                .setCrop(true)
                                                .start(UserFragment.this,getActivity());
                                            break;
                                        case 1:
                                            PhotoPicker.builder()
                                                    .setPhotoCount(1)
                                                    .setPreviewEnabled(false)
                                                    .setCrop(true)
                                                    .setCropXY(1,1)
                                                    .setCropColors(R.color.colorPrimary,R.color.colorPrimaryDark)
                                                    .start(UserFragment.this,getActivity());
                                            break;
                                    }
                                }
                            }).show();

    }

    @Override
    public void onClick(View v) {
        if(TextUtils.isEmpty(token.getToken())){
            switch (v.getId()){
                case R.id.click_self_login:
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                    break;
                default:
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();

            }
        }
        else{
            switch (v.getId()){
                case R.id.image_self_icon:
                    updateIcon();
                    break;
                case R.id.click_self_followed:
                    Intent intent = new Intent(getActivity(),FollowerActivity.class);
                    intent.setFlags(0);
                    startActivity(intent);
                    break;
                case R.id.click_self_follower:
                    Intent intent1 = new Intent(getActivity(),FollowerActivity.class);
                    intent1.setFlags(1);
                    startActivity(intent1);
                    break;
                case R.id.click_self_news:
                    Intent intent2 = new Intent(getActivity(),UserNewsActivity.class);
                    intent2.setFlags(0);
                    startActivity(intent2);
                    break;
                case R.id.click_self_packages:
                    Intent intent3 = new Intent(getActivity(),PackageActivity.class);
                    intent3.setFlags(0);
                    startActivity(intent3);
                    break;
                case R.id.click_self_edit:
                    edit_Profile();
                    break;
                case R.id.click_self_login:
                    Utils.setToken(getActivity(),new Token("",false));
                    startActivity(new Intent(getActivity(),MainActivity.class));
                    getActivity().finish();
                    break;
                case R.id.click_self_setting:
                    startActivity(new Intent(getActivity(),SettingActivity.class));
                    break;
            }
        }
    }

    private void edit_Profile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.edit_profile,null);
        final EditText et_name = (EditText) rootView.findViewById(R.id.et_set_name);
        final EditText et_about_me = (EditText) rootView.findViewById(R.id.et_set_about_me);
        final EditText et_location = (EditText) rootView.findViewById(R.id.et_set_location);
        et_name.setText(user.getName());
        et_location.setText(user.getLocation());
        et_about_me.setText(user.getAbout_me());
        builder.setView(rootView);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name  = et_name.getText().toString();
                String location = et_location.getText().toString();
                String about_me = et_about_me.getText().toString();
                user.setName(name);
                user.setLocation(location);
                user.setAbout_me(about_me);
                update_user();
            }
        });
        builder.show();

    }

    private void update_user(){
        httpUtils.edit_self_profile(user, new HttpCallback() {
            @Override
            public void onResponse(Response response) {
                Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                user = (User) response.body();
                setUser(user);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (resultCode == Activity.RESULT_OK && requestCode == PhotoPicker.CROP_CODE) {
             File file = new File(data.getStringExtra(PhotoPicker.KEY_CAMEAR_PATH));
             HttpUtils httpUtils = new HttpUtils(getActivity());
             httpUtils.upload_icon(file, new HttpCallback() {
                 @Override
                 public void onResponse(Response response) {
                     if (response.code() == 200){
                         user = (User) response.body();
                         setUser(user);
                     }else{
                         Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                     }
                     Log.i("image",response.toString());
                 }

                 @Override
                 public void onFailure(Throwable t) {

                 }
             });

        }
    }
}
