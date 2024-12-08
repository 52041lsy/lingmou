package com.tencent.nanodetncnn;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CallFragment extends Fragment {
    //电话拨打权限
    private static final int REQUEST_CALL_PERMISSION = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu3, container, false);
        Button func1=(Button)view.findViewById(R.id.func1);
        //设置监听：按下则拨给家人
        func1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall1();
            }
        });
        //设置监听：按下则拨给志愿者
        Button func2=(Button)view.findViewById(R.id.func2);
        func2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall2();
            }
        });
        return view;
}
    //打电话
    private void makePhoneCall2() {
        //TODO:从数据库中随机找到志愿者电话号码并拨打
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.70.241:8080/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiService.Call_v service=retrofit.create(ApiService.Call_v.class);
        Call<String> call=service.GetPhone();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String phone=new String();
                if(response.isSuccessful()){
                    phone=response.body();
                }
                Toast.makeText(getActivity(),phone,Toast.LENGTH_SHORT).show();
                assert phone != null;
                if (phone.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "NONE!", Toast.LENGTH_SHORT).show();
                }
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                } else {
                    String dial = "tel:" + phone;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(),"Fail to connect",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void makePhoneCall1() {
        //TODO:查找亲属手机号码并拨打
        String phoneNumber ="13413435260";
        if (phoneNumber.trim().isEmpty()) {
            Toast.makeText(getActivity(), "NONE!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            String dial = "tel:" + phoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

}
