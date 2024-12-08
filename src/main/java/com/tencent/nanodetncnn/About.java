package com.tencent.nanodetncnn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;

public class About extends Activity {
    /*关于我们（静态页面）*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about_us);

        Button exit=this.findViewById(R.id.iv_back);
        exit.setOnClickListener(v -> {
            Intent intent=new Intent(About.this,Main.class);
            startActivity(intent);
        });

    }


}