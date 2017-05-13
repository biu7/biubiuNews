package com.example.qi.biubiunews.user;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.http.ApiService;
import com.example.qi.biubiunews.http.ServiceGenerator;

import java.util.Timer;
import java.util.TimerTask;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_phone;
    private EditText et_code;
    private EditText et_password;
    private EditText et_name;
    private EditText et_city;
    private EditText et_about_me;

    private TextView click_reg;
    private FrameLayout click_send;
    private TextView tv_send;
    private ApiService apiService;

    private int SEND_TIME = 60;

    private Timer timer = null;
    private Handler handler = new Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (SEND_TIME < 1){
                        timer.cancel();
                        timer = null;
                        tv_send.setText("发送验证码");
                        SEND_TIME = 3;
                        click_send.setEnabled(true);
                    }else{
                        tv_send.setText("" + SEND_TIME);
                        click_send.setEnabled(false);
                    }

            }
            return false;
        }
    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        et_phone = (EditText) findViewById(R.id.reg_et_phonenum);
        et_code = (EditText) findViewById(R.id.reg_et_code);
        et_city = (EditText) findViewById(R.id.reg_et_city);
        et_about_me = (EditText) findViewById(R.id.reg_et_about_me);
        et_name = (EditText) findViewById(R.id.reg_et_name);
        et_password = (EditText) findViewById(R.id.et_reg_password);

        click_reg = (TextView) findViewById(R.id.reg_click_reg);
        click_send = (FrameLayout) findViewById(R.id.reg_click_send);
        tv_send = (TextView) findViewById(R.id.reg_et_send);
        click_send.setOnClickListener(this);
        click_reg.setOnClickListener(this);

        apiService = ServiceGenerator.createService(ApiService.class);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reg_click_send:
                send_sms();
                break;
            case R.id.reg_click_reg:
                verify_code();
                break;
        }
    }
    private void verify_code() {
        String phonenum = et_phone.getText().toString();
        String code = et_code.getText().toString();
        String city = et_city.getText().toString();
        String about_me = et_about_me.getText().toString();
        String name = et_name.getText().toString();
        String password = et_password.getText().toString();

        if (TextUtils.isEmpty(phonenum) ||
                TextUtils.isEmpty(code)||
                TextUtils.isEmpty(city)||
                TextUtils.isEmpty(about_me)||
                TextUtils.isEmpty(name)||
                TextUtils.isEmpty(password)){
            Toast.makeText(this, "各选项均不可为空", Toast.LENGTH_SHORT).show();
        }else{
            Call<Void> call = apiService.verify_regist_code(phonenum,code,password,name,city,about_me);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200){
                        Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    }else if(response.code() == 201){
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "暂时无法连接到服务器", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void send_sms() {
        String phonenum = et_phone.getText().toString();
        if (TextUtils.isEmpty(phonenum)){
            Toast.makeText(this, "手机号码不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Call<Void> call = apiService.get_regist_sms(phonenum);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201){
                    Toast.makeText(RegisterActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                    startTimer();
                }else if (response.code() == 203){
                    Toast.makeText(RegisterActivity.this, "手机号已被注册", Toast.LENGTH_SHORT).show();
                }else{
                    Log.i("sms",response.toString());
                    Toast.makeText(RegisterActivity.this, "暂时无法发送短信，可能是每日次数达到上限，请检查服务端设置", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "暂时无法连接到服务器", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SEND_TIME --;
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
        timer.schedule(task,0,1000);
    }


}
