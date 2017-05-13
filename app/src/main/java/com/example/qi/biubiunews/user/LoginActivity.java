package com.example.qi.biubiunews.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.http.ApiService;
import com.example.qi.biubiunews.http.ServiceGenerator;
import com.example.qi.biubiunews.main.view.MainActivity;
import com.example.qi.biubiunews.models.Token;
import com.example.qi.biubiunews.utils.HttpUtils;
import com.example.qi.biubiunews.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_phonenum;
    private EditText et_password;
    private TextView click_reg;
    private TextView click_login;
    private TextView click_reset;

    private HttpUtils httpUtils;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        et_phonenum = (EditText) findViewById(R.id.reset_et_phonenum);
        et_password = (EditText) findViewById(R.id.et_reset_password);
        click_reg = (TextView) findViewById(R.id.login_click_reg);
        click_login = (TextView) findViewById(R.id.login_click_login);
        click_reset = (TextView) findViewById(R.id.login_click_reset);
        click_reg.setOnClickListener(this);
        click_login.setOnClickListener(this);
        click_reset.setOnClickListener(this);
        et_phonenum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        httpUtils = new HttpUtils(this);
    }

    private void getToken(){
        String username = et_phonenum.getText().toString();
        String password = et_password.getText().toString();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "用户名和密码不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiService apiService = ServiceGenerator.createService(ApiService.class,username,password);
        Call<Token> call = apiService.get_token();
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Log.i("login",response.toString());
                if (response.code() == 200){
                    Token token = response.body();
                    Log.i("login",response.body().toString());
                    Utils.setToken(LoginActivity.this,token);
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "登录失败，请检查用户名及密码", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.login_click_login:
                getToken();
                break;
            case R.id.login_click_reg:
                startActivityForResult(new Intent(this,RegisterActivity.class),200);
                break;
            case R.id.login_click_reset:
                startActivity(new Intent(this,ResetActivity.class));
                break;
        }
    }
}
