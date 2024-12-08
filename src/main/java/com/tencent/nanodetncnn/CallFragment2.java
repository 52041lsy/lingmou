package com.tencent.nanodetncnn;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CallFragment2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_3, container, false);
        TextView Cnt1=(TextView)view.findViewById(R.id.volcnt);
        TextView Cnt2=(TextView)view.findViewById(R.id.patcnt);

        //从数据库中读取实时用户数目并显示
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.70.241:8080/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiService.Volunteer_cnt service=retrofit.create(ApiService.Volunteer_cnt.class);
        Call<Integer> call=service.Count();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.body()!=null){
                    Cnt1.setText(response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

        ApiService.Patient_cnt service2=retrofit.create(ApiService.Patient_cnt.class);
        Call<Integer> call2=service2.Count();
        call2.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.body()!=null){
                    Cnt2.setText(response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

        return view;}
}


