package com.tencent.nanodetncnn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.camera.core.processing.concurrent.DualSurfaceProcessorNode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class register extends Activity {
    /*注册页面*/

    private void register(String a,String b,String c,int d) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.70.241:8080/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiService.Register service = retrofit.create(ApiService.Register.class);
        Call<Boolean> call = service.toRegister(a, b, c, d);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (Boolean.TRUE.equals(response.body())) {
                    Toast.makeText(register.this, "Succeed!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(register.this, "Faile!", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(register.this, "Fail to connect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);

        Button returnButton = (Button) this.findViewById(R.id.return_btn);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(register.this,"return!",Toast.LENGTH_SHORT);
                Intent intent=new Intent(register.this,Login.class);
                startActivity(intent);

            }
        });

        //绑定界面原件
        EditText usernameEditText = (EditText) findViewById(R.id.login_input_username);
        EditText passwordEditText = (EditText) findViewById(R.id.login_input_password);
        EditText phoneEditText = (EditText) findViewById(R.id.login_input_phone);
        Button user1Button = (Button) this.findViewById(R.id.user1_btn);
        Button user2Button = (Button) this.findViewById(R.id.user2_btn);
        //登录按钮监听器
        user1Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String a=usernameEditText.getText().toString();
                String b=passwordEditText.getText().toString();
                String c=phoneEditText.getText().toString();
                register(a,b,c,0);
            }
        });

        user2Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String a=usernameEditText.getText().toString();
                String b=passwordEditText.getText().toString();
                String c=phoneEditText.getText().toString();
                register(a,b,c,1);
            }
        });
    }
}
