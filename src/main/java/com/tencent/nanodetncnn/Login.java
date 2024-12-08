package com.tencent.nanodetncnn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends Activity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    //登录功能实现
    protected void login(String a ,String b) {
        //与服务器建立连接，注意查看ip地址是否发生改变
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.70.241:8080/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiService.Login service=retrofit.create(ApiService.Login.class);
        Call<User> call=service.toLogin(a);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //收到响应报文
                if(response.body()!=null){
                    //若ip与pwd均正确，进入主页面
                    if(response.body().getPassword().equals(b)){
                        Toast.makeText(Login.this,"Welcome!",Toast.LENGTH_SHORT).show();
                        //查看用户类型，0则进入患者端，0则进入志愿者端
                        if(response.body().getType()==0){
                            Intent intent=new Intent(Login.this,Main.class);
                            startActivity(intent);
                        }
                        if(response.body().getType()==1){
                            Intent intent=new Intent(Login.this,Main2.class);
                            startActivity(intent);
                        }
                    }
                    else{
                        Toast.makeText(Login.this,"Wrong password!",Toast.LENGTH_SHORT).show();
                    }
                }
                //否则提示错误
                else{
                    Toast.makeText(Login.this,"Your id no exist!",Toast.LENGTH_SHORT).show();
                }

            }
            //未收到报文：提示连接失败
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Login.this,a+","+b+","+"Fail to connect",Toast.LENGTH_SHORT).show();
            }
        });}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        //绑定界面原件
        usernameEditText = findViewById(R.id.login_input_username);
        passwordEditText = findViewById(R.id.login_input_password);
        Button loginButton = (Button) this.findViewById(R.id.login_btn);
        Button registerButton = (Button) this.findViewById(R.id.signup_btn);
        //登录按钮监听器
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String a=usernameEditText.getText().toString();
                String b=passwordEditText.getText().toString();
                login(a,b);
            }
        });
        //注册按钮监听器
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this, register.class);
                startActivity(intent);
            }
        });
}}


