package com.tencent.nanodetncnn;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article);

        // 接收从MainActivity传递过来的数据
        News selectedItem = getIntent().getParcelableExtra("EXTRA_ITEM");
        TextView detailText = findViewById(R.id.detail_text);
        detailText.setText(selectedItem.getTitle());
        TextView contentText = findViewById(R.id.content);
        contentText.setText(selectedItem.getContent());
    }
}
