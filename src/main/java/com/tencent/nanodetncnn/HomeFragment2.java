package com.tencent.nanodetncnn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment2 extends Fragment {
    private ListView listView;
    private MyListAdapter adapter;
    private List<News> items;
    //从数据库获取文章
    protected void getArticle() {
        //与服务器建立连接，注意查看ip地址是否发生改变
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.70.241:8080/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiService.GetArticle service=retrofit.create(ApiService.GetArticle.class);
        Call<List<News>> call=service.getArticle();
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                //收到响应报文
                if(response.body()!=null){
                    items=response.body();
                }

            }
            //未收到报文：提示连接失败
            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Toast.makeText(getContext(),"Fail to connect",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_2, container, false);
        listView = view.findViewById(R.id.listview);
        getArticle();
        adapter = new MyListAdapter(getContext(), items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取点击的项
                News selectedItem = items.get(position);
                // 启动DetailActivity，并传递选中项的数据
                Intent detailIntent = new Intent(getActivity(), Detail.class);
                detailIntent.putExtra("EXTRA_ITEM", selectedItem);
                startActivity(detailIntent);
            }
        });


        return view;
    }
}
